package ru.clevertec.newsManagement.servises.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsManagement.model.Role;
import ru.clevertec.newsManagement.model.Roles;
import ru.clevertec.newsManagement.model.User;
import ru.clevertec.newsManagement.model.dto.UserAuthDto;
import ru.clevertec.newsManagement.model.dto.UserSecurity;
import ru.clevertec.newsManagement.feign.UserServiceFeign;
import ru.clevertec.newsManagement.servises.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserServiceFeign userServiceFeign;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void delete(Long id) {
        userServiceFeign.delete(id);
    }

    @Override
    @Transactional
    public void registrationUserDto(UserAuthDto user, Roles roles) {
        Role role = userServiceFeign.findByRole(roles.name());
        userServiceFeign.save(User
                .builder()
                .username(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .roles(List.of(role)).build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.of(userServiceFeign.findByUsername(username))
                .map(user -> new UserSecurity(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        mapToGrantedAuthority(new ArrayList<>(user.getRoles()))
                )).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    private List<GrantedAuthority> mapToGrantedAuthority(List<Role> userRoles) {
        return userRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }
}
