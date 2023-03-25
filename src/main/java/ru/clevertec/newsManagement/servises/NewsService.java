package ru.clevertec.newsManagement.servises;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.newsManagement.model.News;
import ru.clevertec.newsManagement.model.dto.NewsCreateDto;
import ru.clevertec.newsManagement.model.dto.NewsDto;
import ru.clevertec.newsManagement.model.dto.NewsUpdateDto;

import java.util.List;
import java.util.Map;

public interface NewsService {
    NewsDto saveDto(NewsCreateDto newsDto, UserDetails userDetails);

    NewsDto update(NewsUpdateDto newsDto, Long id, UserDetails userDetails);

    List<NewsDto> findAll(Pageable pageRequest);

    NewsDto findDtoById(Long id);

    void delete(Long id, UserDetails userDetails);

    News findById(Long id);

    List<NewsDto> findAllByParam(Map<String, String> params, Pageable pageRequest);
}

