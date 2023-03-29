package ru.clevertec.newsManagement.mapper;

import org.mapstruct.*;
import ru.clevertec.newsManagement.model.News;
import ru.clevertec.newsManagement.model.dto.NewsCreateDto;
import ru.clevertec.newsManagement.model.dto.NewsDto;
import ru.clevertec.newsManagement.model.dto.NewsUpdateDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    News toEntity(NewsCreateDto tagDto);

    NewsDto toDto(News news);

    List<NewsDto> toDtoList(List<News> news);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    News toPutEntity(Long id, @MappingTarget News news, NewsUpdateDto newsUpdateDto);
}
