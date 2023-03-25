package ru.clevertec.newsManagement.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsManagement.model.dto.CommentCreateDto;
import ru.clevertec.newsManagement.model.dto.CommentDto;
import ru.clevertec.newsManagement.model.dto.CommentUpdateDto;
import ru.clevertec.newsManagement.model.dto.NewsDto;
import ru.clevertec.newsManagement.servises.CommentServices;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "Authentication")
public class CommentController {

    private final CommentServices commentServices;

    @PostMapping
    @Secured({"ROLE_JOURNALIST", "ROLE_ADMIN"})
    public ResponseEntity<CommentDto> add(@RequestBody @Valid CommentCreateDto commentDto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("saving user");
        return new ResponseEntity<>(commentServices.saveDto(commentDto, userDetails),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_JOURNALIST", "ROLE_ADMIN"})
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
    public ResponseEntity<List<CommentDto>> allByAnyParams(@RequestParam(required = false) Map<String, String> params) {
        Pageable pageable = PageRequest.of(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));

        return new ResponseEntity<>(commentServices.findAllByParam(params, pageable),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable @Positive Long id) {
        return new ResponseEntity<>(commentServices.findDtoById(id),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_JOURNALIST", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id,
                       @AuthenticationPrincipal UserDetails userDetails) {
        commentServices.delete(id, userDetails);
    }

}
