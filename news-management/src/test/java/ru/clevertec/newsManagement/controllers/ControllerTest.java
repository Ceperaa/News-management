package ru.clevertec.newsManagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.newsManagement.TestConfig;

@Testcontainers
@ActiveProfiles("prod")
@SpringBootTest(classes = {TestConfig.class},webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(roles = "ADMIN")
public class ControllerTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected WireMockServer wireMockServer;

    @Autowired
    protected ObjectMapper objectMapper;
}
