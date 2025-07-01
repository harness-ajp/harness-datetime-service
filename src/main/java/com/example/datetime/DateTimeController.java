package com.example.datetime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestController
public class DateTimeController {

    private static SplitClientConfig config;
    static SplitFactory splitFactory;
    static SplitClient client;

    @Value("${harness.fme.key}")
    private String HARNESS_FME_KEY;

    @PostConstruct
    public void init() throws IOException, URISyntaxException {
        config = io.split.client.SplitClientConfig.builder()
                .setBlockUntilReadyTimeout(10000)
                .build();
        splitFactory = SplitFactoryBuilder.build(HARNESS_FME_KEY, config);
        client = splitFactory.client();
    }

    @GetMapping("/api/now")
    public String getCurrentDateTime() throws Exception {

        try {
            client.blockUntilReady();
        } catch (TimeoutException | InterruptedException e) {
            // log & handle
        }

        // Search for a specific treatment
        String treatment = client.getTreatment("key", "dark_side");

        Map<String, String> response = new HashMap<>();
        response.put("datetime", ZonedDateTime.now().toString());

        if (treatment.contains("on")) {
            ZonedDateTime original = ZonedDateTime.now(); // Current time in system timezone
            ZonedDateTime newTime = original.withZoneSameInstant(ZoneId.of("Pacific/Fiji")); // +12 from UTC

            response.put("darkSide", newTime.toString());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return "\n" + mapper.writeValueAsString(response) + "\n";
       }

    }
