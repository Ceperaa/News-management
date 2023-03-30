package ru.clevertec.newsManagement.controllers;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsManagement.model.dto.UserAuthDto;
import ru.clevertec.newsManagement.model.dto.UserTokenDto;
import ru.clevertec.newsManagement.security.AuthService;
import ru.clevertec.newsManagement.servises.UserService;

import static ru.clevertec.newsManagement.model.Roles.*;
import static ru.clevertec.newsManagement.model.Roles.ROLE_JOURNALIST;
import static ru.clevertec.newsManagement.model.Roles.ROLE_SUBSCRIBER;
import static ru.clevertec.newsManagement.util.Constants.ADMIN;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;
    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<UserTokenDto> singIn(UserAuthDto authUserDto) {
        return ResponseEntity.ok(auth.singIn(authUserDto));
    }

    @PostMapping("/registration/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrationAdmin(@RequestBody UserAuthDto registrationUserDto) {
        userService.registrationUserDto(registrationUserDto,
                ROLE_ADMIN);
    }

    @PostMapping("/registration/journalist")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrationJournalist(
            @RequestBody UserAuthDto registrationUserDto) {
        userService.registrationUserDto(registrationUserDto,
                ROLE_JOURNALIST);
    }

    @PostMapping("/registration/subscriber")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrationSubscriber(
            @RequestBody UserAuthDto registrationUserDto) {
        userService.registrationUserDto(registrationUserDto,
                ROLE_SUBSCRIBER);
    }

    @DeleteMapping("/{id}")
    @Secured({ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        userService.delete(id);
    }
}
