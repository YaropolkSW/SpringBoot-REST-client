package com.spring.springboot.rest.springbootrestclientapplication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springboot.rest.springbootrestclientapplication.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "data")
public class CommunicationService {
    private final static String STRING_URL =
            "https://nominatim.openstreetmap.org/search?q=%s&country=russia&format=json&polygon_geojson=1";
    private final static String SPACE = " ";
    private final static String UNDERLINE = "_";

    private final static String WRONG_URL = "Wrong URL!";
    private final static String CANT_READ_FROM_URL = "Can't read data from URL!";

    private final RestTemplate restTemplate;

    @Autowired
    public CommunicationService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable
    public Response getData(final String placeName) {
        URL url = null;
        try {
            url = new URL(String.format(STRING_URL, placeName.replaceAll(SPACE, UNDERLINE)));
        } catch (MalformedURLException e) {
            System.out.println(WRONG_URL);
        }

        List<Response> responses = null;
        try {
            responses = new ObjectMapper().readValue(url, ArrayList.class);
        } catch (IOException e) {
            System.out.println(CANT_READ_FROM_URL);
        }

        return new ObjectMapper().convertValue(responses.get(0), Response.class);
    }

//    @Cacheable
//    public Response getData(final String placeName) {
//        final ResponseEntity<List<Response>> responseEntity = restTemplate
//            .exchange(String.format(STRING_URL, placeName.replaceAll(SPACE, UNDERLINE)), HttpMethod.GET, null
//                , new ParameterizedTypeReference<List<Response>>() {});
//
//        return responseEntity.getBody().get(0);
//    }
}
