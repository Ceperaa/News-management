package ru.clevertec.newsManagement;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import ru.clevertec.newsManagement.mapper.CommentMapper;
import ru.clevertec.newsManagement.mapper.NewsMapper;
import ru.clevertec.newsManagement.repository.CommentRepository;
import ru.clevertec.newsManagement.repository.NewsRepository;
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
    @Profile({"test", "repository_test"})
    public CommentService commentService(CommentMapper mapper, NewsService newsService) {
        return new CommentServiceImpl(mock(CommentRepository.class), mapper, newsService);
    }

    @Bean
    @Profile({"test", "repository_test"})
    public NewsService newsService(NewsMapper mapper) {
        return new NewsServiceImpl(mock(NewsRepository.class), mapper);
    }

}