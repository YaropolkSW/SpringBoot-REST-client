package com.spring.springboot.rest.springbootrestclientapplication.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spring.springboot.rest.springbootrestclientapplication.deserializer.GeojsonDesirializer;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = GeojsonDesirializer.class)
public class Geojson {
    private String type;
    private CoordinatePoint coordinatePoint;
}
