package ru.clevertec.newsManagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.newsManagement.TestContainer;
import ru.clevertec.newsManagement.model.News;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class NewsRepositoryTest extends TestContainer {

    @Autowired
    private NewsRepository repository;

    @Test
    void findById() {
        final News news = repository.findById(1L).orElseGet(News::new);
        assertThat(news.getText()).isEqualTo("text");
    }

    @Test
    void findAll() {
        List<News> newsList = repository
                .findAll(CustomerSpecifications.byMultipleParams(Map.of("text", "text")),
                        PageRequest.of(0, 20)).toList();
        assertThat(newsList.get(0).getText()).isEqualTo("text");
    }
}