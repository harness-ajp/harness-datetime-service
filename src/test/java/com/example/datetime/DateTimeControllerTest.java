package com.example.datetime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DateTimeController.class)
public class DateTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeatureFlagService featureFlagService;

    @Test
    void testGetCurrentDateTime() throws Exception {
        mockMvc.perform(get("/api/now"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datetime").exists())
                .andExpect(jsonPath("$.datetime", containsString("T"))); // crude check for ISO-8601 format
    }

    @Test
    void testGetCurrentDateTime_includesDarkSide_whenDarkModeEnabled() throws Exception {
        when(featureFlagService.isDarkModeEnabled()).thenReturn(true);

        mockMvc.perform(get("/api/now"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datetime").exists())
                .andExpect(jsonPath("$.darkSide").exists())
                .andExpect(jsonPath("$.darkSide", containsString("T")));
    }

    @Test
    void testGetCurrentDateTime_omitsDarkSide_whenDarkModeDisabled() throws Exception {
        when(featureFlagService.isDarkModeEnabled()).thenReturn(false);

        mockMvc.perform(get("/api/now"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datetime").exists())
                .andExpect(jsonPath("$.darkSide").doesNotExist());
    }
}