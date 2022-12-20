package com.spring.springboot.rest.springbootrestclientapplication.controller;

import com.spring.springboot.rest.springbootrestclientapplication.service.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class CommunicationController {

    private final CommunicationService communicationService;

    @Autowired
    public CommunicationController(final CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @GetMapping("/")
    public String firstPage() {
        return "index";
    }

    @GetMapping("/info")
    public String getData(
        @RequestParam("placeName") final String placeName,
        final Model model
    ) {

        model.addAttribute("responses", communicationService.getData(placeName));

        return "info";
    }
}
