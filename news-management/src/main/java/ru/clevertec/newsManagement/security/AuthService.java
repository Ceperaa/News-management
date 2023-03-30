package ru.clevertec.newsManagement.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.clevertec.newsManagement.feign.UserServiceFeign;
import ru.clevertec.newsManagement.model.Role;
import ru.clevertec.newsManagement.model.dto.UserAuthDto;
import ru.clevertec.newsManagement.model.dto.UserTokenDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceFeign userServiceFeign;

    public UserTokenDto singIn(UserAuthDto authUserDto) {
        String username = authUserDto.getUsername();
        String password = authUserDto.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserTokenDto userTokenDto = UserTokenDto.builder()
                    .username(username)
                    .token(jwtTokenProvider.createToken(username,
                            getRoleNames(userServiceFeign.findByUsername(username).getRoles()))).build();
            log.debug("Пользователь с именем пользователя " + authUserDto.getUsername() +
                    " успешно авторизован в приложении");
            return userTokenDto;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Неправильное имя пользователя или пароль");
        }
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        return userRoles
                .stream()
                .map(Role::getRole)
                .collect(Collectors.toList());
    }
}
