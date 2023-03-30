package com.example.eurekaclient.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public WireMockServer wireMockServer() {
        return new WireMockServer(9096);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
