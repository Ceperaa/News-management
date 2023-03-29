package ru.clevertec.newsManagement.mapper;

import org.mapstruct.*;
import ru.clevertec.newsManagement.model.Comment;
import ru.clevertec.newsManagement.model.dto.CommentCreateDto;
import ru.clevertec.newsManagement.model.dto.CommentDto;
import ru.clevertec.newsManagement.model.dto.CommentUpdateDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    Comment toEntity(CommentCreateDto createDto);

    @Mapping(source = "news.id", target = "newsId")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment toPutEntity(Long id, @MappingTarget Comment comment, CommentUpdateDto commentUpdateDto);
}
