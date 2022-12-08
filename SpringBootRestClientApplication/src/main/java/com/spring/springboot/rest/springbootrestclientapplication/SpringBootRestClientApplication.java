package com.spring.springboot.rest.springbootrestclientapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringBootRestClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestClientApplication.class, args);
    }

}
