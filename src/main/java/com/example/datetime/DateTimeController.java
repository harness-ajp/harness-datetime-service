package com.example.datetime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DateTimeController {

    @GetMapping("/api/now")
    public Map<String, String> getCurrentDateTime() {
        Map<String, String> response = new HashMap<>();
        response.put("datetime", ZonedDateTime.now().toString());
        return response;
    }
}