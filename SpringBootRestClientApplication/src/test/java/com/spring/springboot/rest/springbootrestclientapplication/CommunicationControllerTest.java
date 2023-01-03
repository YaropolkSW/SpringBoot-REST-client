package com.spring.springboot.rest.springbootrestclientapplication;



import com.spring.springboot.rest.springbootrestclientapplication.controller.CommunicationController;
import com.spring.springboot.rest.springbootrestclientapplication.dto.CoordinatePoint;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Geojson;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Response;
import com.spring.springboot.rest.springbootrestclientapplication.service.CommunicationService;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommunicationControllerTest {
    private final static String URL =
        "https://nominatim.openstreetmap.org/search?q=Samara&country=russia&format=json&polygon_geojson=1";
    private final static String PATH_TO_RESPONSE_BODY = "src/test/resources/response-body.json";
    private final static String EMPTY_LINE = "";

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CommunicationService communicationService;

    @Test
    public void EnableCachingShouldWorkProperly() throws Exception {
        final String responseString =
            String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_RESPONSE_BODY)));

        Mockito.when(restTemplate.getForObject(URL, String.class)).thenReturn(responseString);

        this.mockMvc
            .perform(get("/info?placeName=Samara"))
            .andDo(print())
            .andExpect(status().isOk());

        Assert.assertFalse(cacheManager.getCache("data").get("Samara", Response.class).getName().isEmpty());
    }

    @Test
    public void ControllerMethodGetDataShouldReturnStatus200AndCorrectView() throws Exception {
        final String responseString =
            String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_RESPONSE_BODY)));

        Mockito.when(restTemplate.getForObject(URL, String.class)).thenReturn(responseString);

        this.mockMvc
            .perform(get("/info?placeName=Samara"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("info"));
    }

    @Test
    public void ServiceMethodGetDataShouldReturnCorrectResponse() throws IOException {
        final String responseString =
            String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_RESPONSE_BODY)));

        Mockito.when(restTemplate.getForObject(URL, String.class)).thenReturn(responseString);

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
