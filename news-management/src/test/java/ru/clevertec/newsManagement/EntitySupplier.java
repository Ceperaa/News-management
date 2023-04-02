package ru.clevertec.newsManagement;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.clevertec.newsManagement.model.*;
import ru.clevertec.newsManagement.model.dto.*;

import java.util.ArrayList;
import java.util.List;

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
                .text("text")
                .build();
    }

    public static UserSecurity getUserSecurity() {
        return new UserSecurity(1, "name", "name", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    public static UserAuthDto getUserAuthDto() {
        return UserAuthDto.builder()
                .username("username")
                .password("password")
                .build();
    }

    public static User getUser() {
        return User.builder()
                .id(1L)
                .username("username")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles(List.of(getRole())).build();
    }

    public static Role getRole() {
        return Role.builder().id(1L).role("ROLE_ADMIN").build();
    }
}
