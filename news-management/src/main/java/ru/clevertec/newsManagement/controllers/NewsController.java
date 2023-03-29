package ru.clevertec.newsManagement.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsManagement.model.dto.NewsCreateDto;
import ru.clevertec.newsManagement.model.dto.NewsDto;
import ru.clevertec.newsManagement.model.dto.NewsRequestParamDto;
import ru.clevertec.newsManagement.model.dto.NewsUpdateDto;
import ru.clevertec.newsManagement.servises.NewsService;
import ru.clevertec.newsManagement.util.PageableConfig;

import java.util.List;
import java.util.Map;

import static ru.clevertec.newsManagement.util.Constants.ADMIN;
import static ru.clevertec.newsManagement.util.Constants.JOURNALIST;

@RestController
@RequestMapping("/v1/news")
@Slf4j
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Authentication")
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    @Secured({JOURNALIST, ADMIN})
    public ResponseEntity<NewsDto> add(@RequestBody @Valid NewsCreateDto newsDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(newsService.saveNewsDto(newsDto, userDetails),
                HttpStatus.CREATED);
    }

    @Secured({JOURNALIST, ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<NewsDto> update(@RequestBody @Valid NewsUpdateDto newsDto,
                                          @PathVariable @Positive Long id,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(newsService.updateNews(newsDto, id, userDetails),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NewsDto>> all(Pageable pageRequest) {
        return new ResponseEntity<>(newsService.findAll(pageRequest),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NewsDto>> allByAnyParams( NewsRequestParamDto params) {
        return new ResponseEntity<>(newsService.findAllByParam(params, PageableConfig.of(params.getPage(),params.getSize())),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> findById(@PathVariable @Positive Long id) {
        return new ResponseEntity<>(newsService.findNewsDtoById(id),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured({JOURNALIST, ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive @NumberFormat Long id,
                       @AuthenticationPrincipal UserDetails userDetails) {
        newsService.deleteNews(id, userDetails);
    }
}

