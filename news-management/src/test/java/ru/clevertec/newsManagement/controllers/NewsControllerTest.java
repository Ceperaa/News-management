package ru.clevertec.newsManagement.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.newsManagement.EntitySupplier;
import ru.clevertec.newsManagement.model.dto.NewsDto;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class NewsControllerTest extends ControllerTest {

    private NewsDto newsDtoActual;

    private String jsonObject;

    private String url = "http://localhost:9095/api/v1/news/";

    @BeforeEach
    void setUp() throws JsonProcessingException {
        newsDtoActual = EntitySupplier.getNewsDto();
        jsonObject = objectMapper.writeValueAsString(newsDtoActual);
        wireMockServer.start();
    }

    @Test
    void newsAll() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/news/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<NewsDto> response = restTemplate.getForEntity(url, NewsDto.class);
        NewsDto body = response.getBody();
        assertThat(newsDtoActual.getText()).isEqualTo(body.getText());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void allNewsByAnyParams() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/news/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<NewsDto> response = restTemplate.getForEntity(url, NewsDto.class);
        NewsDto body = response.getBody();
        assertThat(newsDtoActual.getText()).isEqualTo(body.getText());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void findNewsById() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/news/2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<NewsDto> response = restTemplate.getForEntity(url + "2", NewsDto.class);
        NewsDto body = response.getBody();
        assertThat(newsDtoActual.getText()).isEqualTo(body.getText());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void deleteNewsById() {
        wireMockServer.stubFor(delete(urlEqualTo("/api/v1/news/2"))
                .willReturn(aResponse()
                        .withStatus(204)));
        restTemplate.delete(url + "2");
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}