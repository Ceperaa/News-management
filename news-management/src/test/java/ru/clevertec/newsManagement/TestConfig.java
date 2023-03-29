package ru.clevertec.newsManagement;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.newsManagement.servises.CommentService;
import ru.clevertec.newsManagement.servises.NewsService;
import ru.clevertec.newsManagement.servises.impl.CommentServiceImpl;
import ru.clevertec.newsManagement.servises.impl.NewsServiceImpl;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public WireMockServer wireMockServer() {
        return new WireMockServer(9095);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public NewsService newsService() {
        return mock(NewsServiceImpl.class);
    }

    @Bean
    public CommentService commentService() {
        return mock(CommentServiceImpl.class);
    }
}
