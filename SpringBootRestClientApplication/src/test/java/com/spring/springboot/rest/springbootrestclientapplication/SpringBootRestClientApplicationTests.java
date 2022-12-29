package com.spring.springboot.rest.springbootrestclientapplication;

import com.spring.springboot.rest.springbootrestclientapplication.dto.CoordinatePoint;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Geojson;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Response;
import com.spring.springboot.rest.springbootrestclientapplication.service.CommunicationService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SpringBootRestClientApplicationTests {
    private final MockMvc mockMvc;
    private final CacheManager cacheManager;
    private final CommunicationService communicationService;

    @Autowired
    public SpringBootRestClientApplicationTests(
        final MockMvc mockMvc,
        final CacheManager cacheManager,
        final CommunicationService communicationService
    ) {
        this.mockMvc = mockMvc;
        this.cacheManager = cacheManager;
        this.communicationService = communicationService;
    }

    @Test
    public void EnableCachingShouldWorkProperly() throws Exception {
        this.mockMvc
            .perform(get("/info?placeName=Samara"))
            .andDo(print())
            .andExpect(status().isOk());

        Assert.assertFalse(cacheManager.getCache("data").get("Samara", Response.class).getName().isEmpty());
    }

    @Test
    public void ControllerMethodGetDataShouldReturnStatus200AndCorrectView() throws Exception {
        this.mockMvc
            .perform(get("/info?placeName=Samara"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("info"));
    }

    @Test
    public void ServiceMethodGetDataShouldReturnCorrectResponse() {
        final CoordinatePoint coordinatePoint = CoordinatePoint.builder()
            .longitude(50.138)
            .latitude(53.337)
            .build();

        final Geojson geojson = Geojson.builder()
            .type("Polygon")
            .coordinatePoint(coordinatePoint)
            .build();

        final Response expectedResponse = Response.builder()
            .name("Самара, городской округ Самара, Самарская область, Приволжский федеральный округ, 443028, Россия")
            .geojson(geojson)
            .build();

        final Response actualResponse = communicationService.getData("Samara");

        Assert.assertEquals(expectedResponse, actualResponse);
    }
}
