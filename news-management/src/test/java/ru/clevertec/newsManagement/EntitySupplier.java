package ru.clevertec.newsManagement;

import ru.clevertec.newsManagement.model.Comment;
import ru.clevertec.newsManagement.model.News;
import ru.clevertec.newsManagement.model.dto.*;

import java.util.ArrayList;

public class EntitySupplier {

    public static News getNews() {
        return News.builder()
                .id(1L)
                .text("text")
                .title("title")
                .build();
    }

    public static NewsDto getNewsDto() {
        return NewsDto.builder()
                .id(1L)
                .text("text")
                .comments(new ArrayList<>())
                .title("title")
                .build();
    }

    public static NewsCreateDto getNewsCreateDto() {
        return NewsCreateDto.builder()
                .text("text")
                .title("title")
                .build();
    }

    public static NewsUpdateDto getNewsUpdateDto() {
        return NewsUpdateDto.builder()
                .text("text")
                .title("title")
                .build();
    }

    public static Comment getComment() {
        return Comment.builder()
                .id(1L)
                .news(getNews())
                .text("text")
                .username("name")
                .build();
    }

    public static CommentDto getCommentDto() {
        return CommentDto.builder()
                .id(1L)
                .text("text")
                .username("name")
                .build();
    }

    public static CommentCreateDto getCommentCreateDto() {
        return CommentCreateDto.builder()
                .newsId(1L)
                .text("text")
                .build();
    }

    public static CommentUpdateDto getCommentUpdateDto() {
        return CommentUpdateDto.builder()
                .id(1L)
                .text("text")
                .build();
    }

    public static UserSecurity getUserSecurity() {
        return new UserSecurity(1, "name", "name",new ArrayList<>());
    }
}
