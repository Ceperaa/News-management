package ru.clevertec.newsManagement.servises.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.newsManagement.mapper.NewsMapper;
import ru.clevertec.newsManagement.model.News;
import ru.clevertec.newsManagement.model.dto.NewsCreateDto;
import ru.clevertec.newsManagement.model.dto.NewsDto;
import ru.clevertec.newsManagement.model.dto.NewsUpdateDto;
import ru.clevertec.newsManagement.model.dto.UserSecurity;
import ru.clevertec.newsManagement.repository.NewsRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static ru.clevertec.newsManagement.EntitySupplier.*;

@ExtendWith(value = MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl service;

    @Mock
    private NewsRepository repository;

    @Mock
    private NewsMapper mapper;
    private News news;
    private NewsDto newsDto;
    private NewsCreateDto createDto;
    private NewsUpdateDto newsUpdateDto;
    private UserSecurity userSecurity;

    @BeforeEach
    void setUp() {
        news = getNews();
        newsDto = getNewsDto();
        createDto = getNewsCreateDto();
        newsUpdateDto = getNewsUpdateDto();
        userSecurity = getUserSecurity();
    }

    @Test
    void saveDto() {
        given(repository.save(news)).willReturn(news);
        given(mapper.toEntity(createDto)).willReturn(news);
        given(mapper.toDto(news)).willReturn(newsDto);

        NewsDto newsDto = service.saveNewsDto(createDto, userSecurity);

        assertThat(newsDto).isEqualTo(newsDto);
    }

    @Test
    void update() {
        given(repository.save(news)).willReturn(news);
        given(repository.findById(1L)).willReturn(Optional.of(news));
        given(mapper.toDto(news)).willReturn(newsDto);
        given(mapper.toPutEntity(1L, news, newsUpdateDto)).willReturn(news);

        NewsDto update = service.updateNews(newsUpdateDto, 1L, userSecurity);

        assertThat(update).isEqualTo(newsDto);
    }

    @Test
    void findAll() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<News> newsList = List.of(this.news);
        List<NewsDto> newsDto = List.of(this.newsDto);
        given(repository.findAll(pageRequest)).willReturn(new PageImpl<>(newsList));
        given(mapper.toDtoList(newsList)).willReturn(newsDto);

        List<NewsDto> newsDtoList = service.findAll(pageRequest);

        assertThat(newsDtoList).hasSize(1).isEqualTo(newsDto);
    }

    @Test
    void findAllByParam() {
    }

    @Test
    void findDtoById() {
        given(repository.findById(1L)).willReturn(Optional.of(news));
        given(mapper.toDto(news)).willReturn(newsDto);

        NewsDto dtoById = service.findNewsDtoById(1L);

        assertThat(dtoById).isEqualTo(newsDto);
    }

    @Test
    void delete() {
        given(repository.findById(1L)).willReturn(Optional.of(news));

        service.deleteNews(1L, userSecurity);

        verify(repository).delete(news);
    }
}