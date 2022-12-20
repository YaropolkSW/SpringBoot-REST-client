package com.spring.springboot.rest.springbootrestclientapplication.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spring.springboot.rest.springbootrestclientapplication.deserializer.ResponseDesirializer;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@JsonDeserialize(using = ResponseDesirializer.class)
public class Response {
    private double longitude;
    private double latitude;
    private String name;

    public Response(
            final double longitude,
            final double latitude,
            final String name
    ) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }
}
