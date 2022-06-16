package com.kotbot.kot_bot.service;

import com.kotbot.kot_bot.client.TelegramClient;
import com.kotbot.kot_bot.model.Updates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UpdateRecipient {

    private List<Update> failureUpdates = new ArrayList<>();

    private Logger logger = Logger.getLogger(UpdateRecipient.class.getName());

    @Autowired
    private TelegramClient telegramClient;

    @Autowired
    private KafkaTemplate<String, Update> kafkaTemplate;

    @Value("${updates.topic}")
    private String topic;

    @Scheduled(fixedRate = 7000)
    public void writeNewMessages() {
        Updates updates = telegramClient.getUpdates();
        if (updates.isOk() && updates.getResult().length > 0) {
            List<Update> updatesResult = List.of(updates.getResult());
            if (!failureUpdates.isEmpty()) {
                updatesResult.addAll(failureUpdates);
                updatesResult.clear();
            }
            TelegramClient.setOffset(updatesResult.get(updatesResult.size() - 1).getUpdateId() + 1);
            for (Update update : updatesResult) {
                kafkaTemplate.send(topic, update).addCallback((result -> {
                }), (ex) -> {
                    failureUpdates.add(update);
                    logger.log(Level.WARNING, ex.getMessage());
                });
            }

        }
    }

}

