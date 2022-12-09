package com.spring.springboot.rest.springbootrestclientapplication.service;

import com.spring.springboot.rest.springbootrestclientapplication.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@CacheConfig(cacheNames = "data")
public class CommunicationService {
    private final static String URL =
        "https://nominatim.openstreetmap.org/search?q=%s&country=russia&format=json&polygon_geojson=1";
    private final static String SPACE = " ";
    private final static String UNDERLINE = "_";

    private final RestTemplate restTemplate;
    private final CacheManager cacheManager;

    @Autowired
    public CommunicationService(final RestTemplate restTemplate, CacheManager cacheManager) {
        this.restTemplate = restTemplate;
        this.cacheManager = cacheManager;
    }

    @Cacheable
    public List<Response> getData(final String placeName) {
        final ResponseEntity<List<Response>> responseEntity = restTemplate
            .exchange(String.format(URL, placeName.replaceAll(SPACE, UNDERLINE)), HttpMethod.GET, null
                , new ParameterizedTypeReference<List<Response>>() {});

        return responseEntity.getBody();
    }
}
