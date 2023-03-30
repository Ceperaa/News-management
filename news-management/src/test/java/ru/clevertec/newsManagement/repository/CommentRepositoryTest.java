package ru.clevertec.newsManagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.clevertec.newsManagement.model.Comment;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends TestRepository {

    @Autowired
    private CommentRepository repository;

    @Container
    protected static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:14.7");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", CONTAINER::getPassword);
        registry.add("spring.datasource.username", CONTAINER::getUsername);
    }

    @Test
    void findById(){
        final Comment comment = repository.findById(1L).orElseGet(Comment::new);
        assertThat(comment.getText()).isEqualTo("text");
    }

    @Test
    void findAll() {
        final List<Comment> comments = repository
                .findAll(CustomerSpecifications.byMultipleParams(Map.of("text", "2text")),
                        PageRequest.of(0, 20)).toList();
        assertThat(comments.get(0).getText()).isEqualTo("2text");
    }
}