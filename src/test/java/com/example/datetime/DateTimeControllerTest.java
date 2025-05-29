package com.example.datetime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DateTimeController.class)
public class DateTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCurrentDateTime() throws Exception {
        mockMvc.perform(get("/api/now"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datetime").exists())
                .andExpect(jsonPath("$.datetime", containsString("T"))); // crude check for ISO-8601 format
    }
}