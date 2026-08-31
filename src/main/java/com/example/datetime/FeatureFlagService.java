package com.example.datetime;

import io.split.client.SplitClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Thin wrapper around the Harness FME (Split) {@link SplitClient} used to evaluate
 * feature flags. Evaluation fails closed (flag treated as disabled) on any error or
 * unrecognized treatment (e.g. "control"), so callers never need to handle exceptions.
 */
@Service
public class FeatureFlagService {

    private static final Logger logger = LoggerFactory.getLogger(FeatureFlagService.class);

    private static final String TREATMENT_ON = "on";

    // Evaluation isn't per-end-user for this service, so a constant bucketing key is used.
    private static final String BUCKETING_KEY = "harness-datetime-service";

    private final SplitClient splitClient;
    private final String darkModeFlagName;

    public FeatureFlagService(SplitClient splitClient,
                               @Value("${harness.fme.flags.dark-mode}") String darkModeFlagName) {
        this.splitClient = splitClient;
        this.darkModeFlagName = darkModeFlagName;
    }

    public boolean isDarkModeEnabled() {
        try {
            String treatment = splitClient.getTreatment(BUCKETING_KEY, darkModeFlagName);
            return TREATMENT_ON.equals(treatment);
        } catch (Exception e) {
            logger.warn("Failed to evaluate Harness FME flag '{}'; defaulting to disabled.", darkModeFlagName, e);
            return false;
        }
    }
}
