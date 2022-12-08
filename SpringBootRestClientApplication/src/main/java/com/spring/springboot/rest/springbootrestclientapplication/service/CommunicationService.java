package com.spring.springboot.rest.springbootrestclientapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springboot.rest.springbootrestclientapplication.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommunicationService {
    private final static String URL = "https://nominatim.openstreetmap.org/" +
        "search?q=%s&country=russia&format=json&polygon_geojson=1";

    private final RestTemplate restTemplate;

    @Autowired
    public CommunicationService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Response getData(final String placeName) throws JsonProcessingException {
        final ResponseEntity<String> responseEntity = restTemplate
            .exchange(String.format(URL, placeName), HttpMethod.GET, null
                , new ParameterizedTypeReference<String>() {});

        return new ObjectMapper()
            .readerFor(Response.class)
            .readValue(responseEntity.getBody());
    }
}
