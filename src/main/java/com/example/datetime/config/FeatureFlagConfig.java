package com.example.datetime.config;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeoutException;

/**
 * Wires up the Harness FME (Split) Java SDK client used to evaluate feature flags,
 * such as the "Dark Mode" flag exposed via {@link com.example.datetime.FeatureFlagService}.
 */
@Configuration
public class FeatureFlagConfig {

    private static final Logger logger = LoggerFactory.getLogger(FeatureFlagConfig.class);

    private static final int BLOCK_UNTIL_READY_TIMEOUT_MS = 3000;

    @Bean(destroyMethod = "destroy")
    public SplitClient splitClient(@Value("${harness.fme.key}") String apiKey) {
        try {
            SplitClientConfig config = SplitClientConfig.builder()
                    .setBlockUntilReadyTimeout(BLOCK_UNTIL_READY_TIMEOUT_MS)
                    .build();

            SplitFactory splitFactory = SplitFactoryBuilder.build(apiKey, config);
            SplitClient client = splitFactory.client();

            try {
                client.blockUntilReady();
            } catch (TimeoutException | InterruptedException e) {
                logger.warn("Harness FME client was not ready within {}ms; feature flags will fail closed until ready.",
                        BLOCK_UNTIL_READY_TIMEOUT_MS, e);
            }

            return client;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize Harness FME (Split) client", e);
        }
    }
}
