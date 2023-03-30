package ru.clevertec.newsManagement.servises.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsManagement.cache.aop.annotations.GetCache;
import ru.clevertec.newsManagement.cache.aop.annotations.PutCache;
import ru.clevertec.newsManagement.cache.aop.annotations.RemoveCache;
import ru.clevertec.newsManagement.exception.EntityNotFoundException;
import ru.clevertec.newsManagement.mapper.CommentMapper;
import ru.clevertec.newsManagement.model.Comment;
import ru.clevertec.newsManagement.model.News;
import ru.clevertec.newsManagement.model.Roles;
import ru.clevertec.newsManagement.model.dto.*;
import ru.clevertec.newsManagement.repository.CommentRepository;
import ru.clevertec.newsManagement.repository.CustomerSpecifications;
import ru.clevertec.newsManagement.servises.CommentService;
import ru.clevertec.newsManagement.servises.NewsService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl extends AbstractService implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final NewsService newsService;

    @PutCache(sourceFieldEntity = "newsId", targetFieldEntity = "comments", typeEntity = NewsDto.class)
    @Transactional
    public CommentDto saveDto(CommentCreateDto commentDto, UserDetails userDetails) {

        Comment comment = mapper.toEntity(commentDto);
        comment.setNews(newsService.findById(commentDto.getNewsId()));
        comment.setUsername(userDetails.getUsername());
        return mapper.toDto(repository.save(comment));
    }

    @Transactional
    @PutCache
    public CommentDto update(CommentUpdateDto commentDto, Long id, UserDetails userDetails) {
        final Comment comment = findById(id);
        checkAuthorization(comment.getUsername(), userDetails, Roles.ROLE_SUBSCRIBER);
        return mapper.toDto(
                repository.save(
                        mapper.toPutEntity(id, comment, commentDto)));
    }

    @Override
    public List<CommentDto> findAll(Pageable pageRequest) {
        return mapper.toDtoList(repository.findAll(pageRequest).toList());
    }

    @Override
    @GetCache
    public CommentDto findCommentById(Long id) {
        return mapper.toDto(findById(id));
    }

    private Comment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(News.class, id));
    }

    public List<CommentDto> findAllByParam(CommentRequestParamDto params, Pageable pageRequest) {
        Map<String, String> map = new ObjectMapper().convertValue(params, Map.class);
        Page<Comment> comments = repository.findAll(CustomerSpecifications.<Comment>byMultipleParams(map),
                pageRequest);
        List<Comment> comments1 = comments.toList();
        return mapper.toDtoList(comments1);
    }

    @Override
    @Transactional
    @RemoveCache(nameKey = "CommentDto",
            sourceFieldEntity = "newsId",
            targetFieldEntity = "comments",
            typeEntity = NewsDto.class)
    public void delete(Long id, UserDetails userDetails) {
        Comment comment = findById(id);
        checkAuthorization(comment.getUsername(), userDetails, Roles.ROLE_SUBSCRIBER);
        repository.delete(comment);
    }
}
