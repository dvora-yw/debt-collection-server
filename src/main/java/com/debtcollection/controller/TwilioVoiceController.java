package com.debtcollection.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twilio")
public class TwilioVoiceController {

    @GetMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
    public String voice() {
        return """
            <Response>
                <Say language="he-IL">
                    יש לך חוב פתוח. אנא היכנסי למערכת להסדרה.
                </Say>
            </Response>
        """;
    }
}
