package com.spring.springboot.rest.springbootrestclientapplication.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private String name;
    private Geojson geojson;

    @JsonCreator
    public Response(
            @JsonProperty("display_name") final String name,
            @JsonProperty("geojson") final Geojson geojson
    ) {
        this.name = name;
        this.geojson = geojson;
    }
}
