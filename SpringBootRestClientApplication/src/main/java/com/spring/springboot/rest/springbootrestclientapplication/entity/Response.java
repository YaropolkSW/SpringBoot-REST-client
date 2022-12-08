package com.spring.springboot.rest.springbootrestclientapplication.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Response {
    private final float latitude;
    private final float longitude;
    private final String name;

    @JsonCreator
    public Response(
        @JsonProperty("lat") final float latitude,
        @JsonProperty("lon") final float longitude,
        @JsonProperty("display_name") final String name
    ) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
}
