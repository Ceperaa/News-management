package ru.clevertec.newsManagement.servises;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.newsManagement.model.dto.CommentCreateDto;
import ru.clevertec.newsManagement.model.dto.CommentDto;
import ru.clevertec.newsManagement.model.dto.CommentRequestParamDto;
import ru.clevertec.newsManagement.model.dto.CommentUpdateDto;

import java.util.List;
import java.util.Map;

public interface CommentService {

    CommentDto saveDto(CommentCreateDto commentDto, UserDetails userDetails);

    CommentDto update(CommentUpdateDto commentDto, Long id, UserDetails userDetails);

    List<CommentDto> findAll(Pageable pageRequest);

    CommentDto findCommentById(Long id);

    void delete(Long id, UserDetails userDetails);

    List<CommentDto> findAllByParam(CommentRequestParamDto params, Pageable pageRequest);
}
