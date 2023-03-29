package ru.clevertec.newsManagement.util;

import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.Optional;

public class PageableConfig {

    public static PageRequest of(Integer page, Integer size) {
        Integer pageValue = 0;
        Integer sizeValue = 20;
        PageRequest pageRequest = PageRequest.of(
                Optional.ofNullable(page)
                        .filter((pag) -> pag != 0)
                        .orElseGet(() -> pageValue),
                Optional.ofNullable(size)
                        .filter((siz) -> siz != 0)
                        .orElseGet(() -> sizeValue));
        return pageRequest;
    }
}
