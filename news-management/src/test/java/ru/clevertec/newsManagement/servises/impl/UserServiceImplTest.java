package ru.clevertec.newsManagement.servises.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.newsManagement.EntitySupplier;
import ru.clevertec.newsManagement.TestConfig;
import ru.clevertec.newsManagement.model.Role;
import ru.clevertec.newsManagement.model.Roles;
import ru.clevertec.newsManagement.model.User;
import ru.clevertec.newsManagement.model.dto.UserAuthDto;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureTestDatabase
class UserServiceImplTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected WireMockServer wireMockServer;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        wireMockServer.start();
    }

    @Test
    void testRegistrationUserDto() throws JsonProcessingException {
        String jsonUserSecur = objectMapper.writeValueAsString(EntitySupplier.getUser());
        wireMockServer.stubFor(get(urlPathEqualTo("/user"))
                .withQueryParam("username",equalTo("username") )
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonUserSecur)
                        .withStatus(200)));

        UserDetails username = userService.loadUserByUsername("username");
        assertThat(username.getUsername()).isEqualTo("username");
    }

}