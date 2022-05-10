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

    @Scheduled(fixedRate = 10000)
    public void writeNewMessages() {
        Updates updates = telegramClient.getUpdates();

        if (updates.isOk() && updates.getResult().length > 0) {
            Update[] updatesResult = updates.getResult();
            TelegramClient.setOffset(updatesResult[updatesResult.length - 1].getUpdateId() + 1);
            for (int i = 0; i < updatesResult.length; i++) {
                int tempIndex = i;
                kafkaTemplate.send(topic, updatesResult[i]).addCallback((result -> {
                }), (ex) -> {
                    failureUpdates.add(updatesResult[tempIndex]);
                    logger.log(Level.WARNING, ex.getMessage());
                });
            }
        }

    }


}
