package com.example.eurekaclient.controllers;

import com.example.eurekaclient.model.Role;
import com.example.eurekaclient.model.User;
import com.example.eurekaclient.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class UserControllerTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected WireMockServer wireMockServer;

    @Autowired
    protected ObjectMapper objectMapper;

    private String jsonObject;
    private User user;
    private Role role;
    private final String url = "http://localhost:9096/api/v1/user";

    @Container
    protected static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:14.7");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", CONTAINER::getPassword);
        registry.add("spring.datasource.username", CONTAINER::getUsername);
    }

    @BeforeEach
    void setUp() throws JsonProcessingException {
        role = Role.builder().id(1L).role("ROLE_ADMIN").build();
        user = User.builder().id(1L).username("name").password("pass").build();
        jsonObject = objectMapper.writeValueAsString(user);
        wireMockServer.start();
    }

    @Test
    void findByUsername() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/user?username=username"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<User> response = restTemplate.getForEntity(url + "?username=username", User.class);
        User body = response.getBody();
        assertThat(user.getUsername()).isEqualTo(body.getUsername());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void findById() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/user/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<User> response = restTemplate.getForEntity(url + "/1", User.class);
        User body = response.getBody();
        assertThat(user.getUsername()).isEqualTo(body.getUsername());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void findByRole() throws JsonProcessingException {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/user/role?username=username"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody( objectMapper.writeValueAsString(role))
                        .withStatus(200)));
        ResponseEntity<Role> response = restTemplate.getForEntity(url + "/role?username=username", Role.class);
        Role body = response.getBody();
        assertThat(role.getRole()).isEqualTo(body.getRole());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}