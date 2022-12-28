package com.spring.springboot.rest.springbootrestclientapplication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoordinatePoint {
    private final double longitude;
    private final double latitude;
}
