package com.spring.springboot.rest.springbootrestclientapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Response;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@CacheConfig
public class CommunicationService {
    private final static String STRING_URL =
            "https://nominatim.openstreetmap.org/search?q=%s&country=russia&format=json&polygon_geojson=1";
    private final static String SPACE = " ";
    private final static String UNDERLINE = "_";

    private final static String CANT_READ_FROM_RESPONSE_ENTITY = "Can't read data from Response Entity!";

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

    public CommunicationService(
        final ObjectMapper mapper,
        final RestTemplate restTemplate
    ) {
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "data", key = "#placeName")
    public Response getData(final String placeName) {
        final String url = String.format(STRING_URL, placeName.replaceAll(SPACE, UNDERLINE));
        final String responseEntity = restTemplate.getForObject(url, String.class);

        List<Response> responses = null;
        try {
            responses = mapper.readValue(responseEntity, new TypeReference<List<Response>>() {});
        } catch (JsonProcessingException e) {
            System.out.println(CANT_READ_FROM_RESPONSE_ENTITY);
        }

        return responses.get(0);
    }
}
