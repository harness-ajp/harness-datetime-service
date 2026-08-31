package com.example.datetime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DateTimeController {

    @GetMapping("/api/now")
    public String getCurrentDateTime() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("datetime", ZonedDateTime.now().toString());

        ZonedDateTime original = ZonedDateTime.now(); // Current time in system timezone
        ZonedDateTime newTime = original.withZoneSameInstant(ZoneId.of("Pacific/Fiji")); // +12 from UTC

        response.put("darkSide", newTime.toString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return "\n" + mapper.writeValueAsString(response) + "\n";
    }

}
