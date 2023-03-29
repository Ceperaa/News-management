package ru.clevertec.newsManagement.servises.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.newsManagement.mapper.CommentMapper;
import ru.clevertec.newsManagement.model.Comment;
import ru.clevertec.newsManagement.model.News;
import ru.clevertec.newsManagement.model.dto.CommentCreateDto;
import ru.clevertec.newsManagement.model.dto.CommentDto;
import ru.clevertec.newsManagement.model.dto.CommentUpdateDto;
import ru.clevertec.newsManagement.model.dto.UserSecurity;
import ru.clevertec.newsManagement.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static ru.clevertec.newsManagement.EntitySupplier.*;

@ExtendWith(value = MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl service;

    @Mock
    private CommentMapper mapper;

    @Mock
    private NewsServiceImpl newsService;

    @Mock
    private CommentRepository repository;
    private Comment comment;
    private CommentDto commentDto;
    private CommentUpdateDto commentUpdateDto;
    private CommentCreateDto commentCreateDto;
    private News news;
    private UserSecurity userSecurity;

    @BeforeEach
    void setUp() {
        news = getNews();
        comment = getComment();
        commentDto = getCommentDto();
        commentCreateDto = getCommentCreateDto();
        commentUpdateDto = getCommentUpdateDto();
        userSecurity = getUserSecurity();
    }

    @Test
    void saveDto() {
        given(mapper.toEntity(commentCreateDto)).willReturn(comment);
        given(newsService.findById(1L)).willReturn(news);
        given(repository.save(comment)).willReturn(comment);
        given(mapper.toDto(comment)).willReturn(commentDto);

        CommentDto commentDto = service.saveDto(commentCreateDto, userSecurity);

        assertThat(commentDto).isEqualTo(commentDto);
    }

    @Test
    void update() {
        given(repository.save(comment)).willReturn(comment);
        given(repository.findById(1L)).willReturn(Optional.of(comment));
        given(mapper.toDto(comment)).willReturn(commentDto);
        given(mapper.toPutEntity(1L, comment, commentUpdateDto)).willReturn(comment);

        CommentDto update = service.update(commentUpdateDto, 1L, userSecurity);

        assertThat(update).isEqualTo(commentDto);
    }

    @Test
    void findAll() {
        PageRequest page = PageRequest.of(0, 20);
        List<Comment> comment = List.of(this.comment);
        List<CommentDto> commentDto = List.of(this.commentDto);
        given(repository.findAll(page)).willReturn(new PageImpl<>(comment));
        given(mapper.toDtoList(comment)).willReturn(commentDto);

        List<CommentDto> all = service.findAll(page);

        assertThat(all).hasSize(1).isEqualTo(commentDto);
    }

    @Test
    void findDtoById() {
        given(repository.findById(1L)).willReturn(Optional.of(comment));
        given(mapper.toDto(comment)).willReturn(commentDto);

        CommentDto dtoById = service.findCommentById(1L);

        assertThat(dtoById).isEqualTo(commentDto);
    }

    @Test
    void delete() {
        given(repository.findById(1L)).willReturn(Optional.of(comment));

        service.delete(1L, userSecurity);

        verify(repository).delete(comment);
    }
}