package ru.clevertec.newsManagement.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsManagement.model.dto.CommentCreateDto;
import ru.clevertec.newsManagement.model.dto.CommentDto;
import ru.clevertec.newsManagement.model.dto.CommentRequestParamDto;
import ru.clevertec.newsManagement.model.dto.CommentUpdateDto;
import ru.clevertec.newsManagement.servises.CommentService;
import ru.clevertec.newsManagement.util.PageableConfig;

import java.util.List;
import java.util.Map;

import static ru.clevertec.newsManagement.util.Constants.ADMIN;
import static ru.clevertec.newsManagement.util.Constants.SUBSCRIBER;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
@SecurityRequirement(name = "Authentication")
public class CommentController {

    private final CommentService commentServices;

    @PostMapping
    @Secured({SUBSCRIBER, ADMIN})
    public ResponseEntity<CommentDto> add(@RequestBody @Valid CommentCreateDto commentDto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(commentServices.saveDto(commentDto, userDetails),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Secured({SUBSCRIBER, ADMIN})
    public ResponseEntity<CommentDto> update(@RequestBody @Valid CommentUpdateDto commentDto,
                                             @PathVariable @Positive Long id,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(commentServices.update(commentDto, id, userDetails),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> all(Pageable pageRequest) {
        return new ResponseEntity<>(commentServices.findAll(pageRequest),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentDto>> allByAnyParams(CommentRequestParamDto params) {
        return new ResponseEntity<>(commentServices.findAllByParam(params,
                PageableConfig.of(params.getPage(), params.getSize())),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable @Positive Long id) {
        return new ResponseEntity<>(commentServices.findCommentById(id),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured({SUBSCRIBER, ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id,
                       @AuthenticationPrincipal UserDetails userDetails) {
        commentServices.delete(id, userDetails);
    }

}
