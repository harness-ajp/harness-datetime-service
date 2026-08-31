package com.example.datetime;

import io.split.client.SplitClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FeatureFlagServiceTest {

    private static final String DARK_MODE_FLAG_NAME = "dark_mode";

    private SplitClient splitClient;
    private FeatureFlagService featureFlagService;

    @BeforeEach
    void setUp() {
        splitClient = mock(SplitClient.class);
        featureFlagService = new FeatureFlagService(splitClient, DARK_MODE_FLAG_NAME);
    }

    @Test
    void isDarkModeEnabled_returnsTrue_whenTreatmentIsOn() {
        when(splitClient.getTreatment(anyString(), anyString())).thenReturn("on");

        assertTrue(featureFlagService.isDarkModeEnabled());
    }

    @Test
    void isDarkModeEnabled_returnsFalse_whenTreatmentIsOff() {
        when(splitClient.getTreatment(anyString(), anyString())).thenReturn("off");

        assertFalse(featureFlagService.isDarkModeEnabled());
    }

    @Test
    void isDarkModeEnabled_returnsFalse_whenTreatmentIsControl() {
        when(splitClient.getTreatment(anyString(), anyString())).thenReturn("control");

        assertFalse(featureFlagService.isDarkModeEnabled());
    }

    @Test
    void isDarkModeEnabled_returnsFalse_whenClientThrowsException() {
        when(splitClient.getTreatment(anyString(), anyString())).thenThrow(new RuntimeException("SDK error"));

        assertFalse(featureFlagService.isDarkModeEnabled());
    }
}
