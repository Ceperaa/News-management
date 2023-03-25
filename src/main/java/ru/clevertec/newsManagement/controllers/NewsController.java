package ru.clevertec.newsManagement.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsManagement.model.Roles;
import ru.clevertec.newsManagement.model.dto.CommentDto;
import ru.clevertec.newsManagement.model.dto.NewsCreateDto;
import ru.clevertec.newsManagement.model.dto.NewsDto;
import ru.clevertec.newsManagement.model.dto.NewsUpdateDto;
import ru.clevertec.newsManagement.servises.NewsService;

import javax.xml.bind.ValidationException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/** @noinspection ALL*/
@RestController
@RequestMapping("/v1/news")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "Authentication")
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    @Secured({"ROLE_JOURNALIST","ROLE_ADMIN"})
    public ResponseEntity<NewsDto> add(@RequestBody @Valid NewsCreateDto newsDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(newsService.saveDto(newsDto, userDetails),
                HttpStatus.CREATED);
    }

    @Secured({"ROLE_JOURNALIST","ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<NewsDto> update(@RequestBody @Valid NewsUpdateDto newsDto,
                                          @PathVariable @Positive Long id,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(newsService.update(newsDto, id, userDetails),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NewsDto>> all(Pageable pageRequest) {
        return new ResponseEntity<>(newsService.findAll(pageRequest),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NewsDto>> allByAnyParams(@RequestParam(required = false) Map<String, String> params) {
        Pageable pageable = PageRequest.of(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        params.remove("page");
        params.remove("size");
        return new ResponseEntity<>(newsService.findAllByParam(params, pageable),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> findById(@PathVariable @Positive Long id) {
        return new ResponseEntity<>(newsService.findDtoById(id),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_JOURNALIST","ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id,
                       @AuthenticationPrincipal UserDetails userDetails) {
        newsService.delete(id, userDetails);
    }

}

//  @PostFilter("filterObject.role.name().equels('admin')")
//@PostFilter("@newsService.fingById(filterObject.coment.id()).isPresent")
// @PostAuthorize("returnObject")
//  @PreAuthorize("#username == authentication.principal.username")
// @PreAuthorize("@newsService.findById(#id).getUsername() == authentication.principal.name")
