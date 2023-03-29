package ru.clevertec.newsManagement.servises.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsManagement.cache.aop.annotations.GetCache;
import ru.clevertec.newsManagement.cache.aop.annotations.PutCache;
import ru.clevertec.newsManagement.cache.aop.annotations.RemoveCache;
import ru.clevertec.newsManagement.exception.EntityNotFoundException;
import ru.clevertec.newsManagement.mapper.NewsMapper;
import ru.clevertec.newsManagement.model.Comment;
import ru.clevertec.newsManagement.model.News;
import ru.clevertec.newsManagement.model.dto.*;
import ru.clevertec.newsManagement.repository.CustomerSpecifications;
import ru.clevertec.newsManagement.repository.NewsRepository;
import ru.clevertec.newsManagement.servises.NewsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.clevertec.newsManagement.model.Roles.ROLE_JOURNALIST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl extends AbstractService implements NewsService {

    private final NewsRepository repository;
    private final NewsMapper mapper;

    @Override
    @Transactional
    @PutCache
    public NewsDto saveNewsDto(NewsCreateDto newsDto, UserDetails userDetails) {
        News news = mapper.toEntity(newsDto);
        news.setUsername(userDetails.getUsername());
        return mapper.toDto(
                repository.save(news));
    }

    @Override
    @Transactional
    @PutCache
    public NewsDto updateNews(NewsUpdateDto commentDto, Long id, UserDetails userDetails) {
        News news = findById(id);
        checkAuthorization(news.getUsername(), userDetails, ROLE_JOURNALIST);
        return mapper.toDto(
                repository.save(
                        mapper.toPutEntity(id, news, commentDto)));
    }

    @Override
    public List<NewsDto> findAll(Pageable pageRequest) {
        return mapper.toDtoList(repository.findAll(pageRequest).toList());
    }

    public List<NewsDto> findAllByParam(NewsRequestParamDto params, Pageable pageRequest) {
        Map<String, String> map = new ObjectMapper().convertValue(params, Map.class);
        return mapper.toDtoList(repository
                .findAll(CustomerSpecifications.<News>byMultipleParams(map),
                        pageRequest).toList());
    }

    @Override
    @GetCache
    public NewsDto findNewsDtoById(Long id) {
        final NewsDto newsDto = mapper.toDto(findById(id));
        newsDto.getComments().forEach(com -> com.setNewsId(id));
        return newsDto;
    }

    public News findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(News.class, id));
    }

    @Override
    @Transactional
    @RemoveCache(nameKey = "NewsDto")
    public void deleteNews(Long id, UserDetails userDetails) {
        News news = findById(id);
        checkAuthorization(news.getUsername(), userDetails, ROLE_JOURNALIST);
        repository.delete(news);
    }
}
