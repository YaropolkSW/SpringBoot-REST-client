package com.spring.springboot.rest.springbootrestclientapplication.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class CoordinatePoint {
    private final double longitude;
    private final double latitude;
}
