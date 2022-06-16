package com.kotbot.kot_bot.client;

import com.kotbot.kot_bot.model.Updates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramClient {

    private static final int UPDATES_LIMIT = 100;
    private static final int TIMEOUT = 4;
    @lombok.Setter
    private static int offset = 0;

    @Value("${telegram.url}")
    private String url;

    public Updates getUpdates() {
        RestTemplate restTemplate = new RestTemplate();
        String updateUrl = url + "/getUpdates";
        String query = UriComponentsBuilder
                .fromHttpUrl(updateUrl)
                .queryParam("offset", "{offset}")
                .queryParam("limit", "{limit}")
                .queryParam("timeout", "{timeout}")
                .encode()
                .toUriString();
        Map<String, Integer> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", UPDATES_LIMIT);
        params.put("timeout", TIMEOUT);

        return restTemplate.exchange(query, HttpMethod.GET, null, Updates.class, params).getBody();
    }

}
