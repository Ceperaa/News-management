package ru.clevertec.newsManagement.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.clevertec.newsManagement.EntitySupplier;
import ru.clevertec.newsManagement.model.dto.CommentDto;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class CommentControllerTest extends ControllerTest {

    private CommentDto commentDtoActual;
    private String jsonObject;
    private final String url = "http://localhost:9095/api/v1/comments/";

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
        commentDtoActual = EntitySupplier.getCommentDto();
        jsonObject = objectMapper.writeValueAsString(commentDtoActual);
        wireMockServer.start();
    }

    @Test
    void allComment() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/comments/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withTransformerParameters(Map.of("page", "0", "size", "1"))
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<CommentDto> response = restTemplate.getForEntity(url, CommentDto.class);
        CommentDto body = response.getBody();
        assertThat(commentDtoActual.getText()).isEqualTo(body.getText());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void allCommentByAnyParams() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/comments/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<CommentDto> response = restTemplate.getForEntity(url, CommentDto.class);
        CommentDto body = response.getBody();
        assertThat(commentDtoActual.getText()).isEqualTo(body.getText());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void findCommentById() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/comments/2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject)
                        .withStatus(200)));
        ResponseEntity<CommentDto> response = restTemplate.getForEntity(url+"2", CommentDto.class);
        CommentDto body = response.getBody();
        assertThat(commentDtoActual.getText()).isEqualTo(body.getText());
        assertThat(200).isEqualTo(response.getStatusCode().value());
    }

    @Test
    void deleteCommentById() {
        wireMockServer.stubFor(delete(urlEqualTo("/api/v1/comments/10"))
                .willReturn(aResponse()
                        .withStatus(204)));
        restTemplate.delete(url + "10");
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}