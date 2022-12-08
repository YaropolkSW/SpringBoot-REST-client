package com.spring.springboot.rest.springbootrestclientapplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.springboot.rest.springbootrestclientapplication.service.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CommunicationController {

    private final CommunicationService communicationService;

    @Autowired
    public CommunicationController(final CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @GetMapping("/")
    public String firstPage(final Model model) {
        model.addAttribute("placeName", new String());
        return "index";
    }

    @GetMapping("/info")
    public String getData(
        @ModelAttribute("placeName") final String placeName,
        final Model model
    ) throws JsonProcessingException {

        model.addAttribute("response", communicationService.getData(placeName));

        return "info";
    }
}
