package ru.clevertec.newsManagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.newsManagement.TestContainer;
import ru.clevertec.newsManagement.model.Comment;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends TestContainer {

    @Autowired
    private CommentRepository repository;

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