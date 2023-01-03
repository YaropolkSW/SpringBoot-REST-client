package com.spring.springboot.rest.springbootrestclientapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springboot.rest.springbootrestclientapplication.dto.CoordinatePoint;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Geojson;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Response;
import com.spring.springboot.rest.springbootrestclientapplication.service.CommunicationService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SpringBootRestClientApplicationTests {
    private final static String URL =
        "https://nominatim.openstreetmap.org/search?q=Samara&country=russia&format=json&polygon_geojson=1";
    private final static String PATH_TO_RESPONSE_BODY = "src/test/resources/response-body.txt";
    private final static String EMPTY_LINE = "";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CacheManager cacheManager;
    @MockBean
    private RestTemplate restTemplate;
    private final CommunicationService communicationService = new CommunicationService(mapper, restTemplate);

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
    public void ServiceMethodGetDataShouldReturnCorrectResponse() throws IOException {
        String responseBody = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_RESPONSE_BODY)));

        Mockito
            .when(restTemplate.getForObject(URL, String.class))
            .thenReturn(responseBody);

        final CoordinatePoint coordinatePoint = CoordinatePoint.builder()
            .longitude(50.136)
            .latitude(53.337)
            .build();

        final Geojson geojson = Geojson.builder()
            .type("Polygon")
            .coordinatePoint(coordinatePoint)
            .build();

        final Response expectedResponse = Response.builder()
            .name("городской округ Самара, Самарская область, Приволжский федеральный округ, Россия")
            .geojson(geojson)
            .build();

        final Response actualResponse = communicationService.getData("Samara");

        Assert.assertEquals(expectedResponse, actualResponse);
    }
}
