package ru.clevertec.user_security.controllers;

import ru.clevertec.user_security.repositiry.RoleRepository;
import ru.clevertec.user_security.repositiry.UserRepository;
import ru.clevertec.user_security.service.UserService;
import ru.clevertec.user_security.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

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

    @Bean
    @Profile({"controller_test","test"})
    public UserService userService() {
        return new UserServiceImpl(mock(UserRepository.class),mock(RoleRepository.class));
    }
}
