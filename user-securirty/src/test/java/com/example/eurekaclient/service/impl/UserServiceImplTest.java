package com.example.eurekaclient.service.impl;

import com.example.eurekaclient.model.Role;
import com.example.eurekaclient.model.User;
import com.example.eurekaclient.repositiry.RoleRepository;
import com.example.eurekaclient.repositiry.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder().id(1L).role("ROLE_ADMIN").build();
        user = User.builder().id(1L).username("name").password("pass").build();
    }

    @Test
    void findByUsername() {
        given(userRepository.findByUsername("name")).willReturn(Optional.of(user));

        User byId = userService.findByUsername("name");

        assertThat(byId).isEqualTo(user);
    }

    @Test
    void findById() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        User byId = userService.findById(1L);

        assertThat(byId).isEqualTo(user);
    }

    @Test
    void findByRoleName() {
        given(roleRepository.findByRole("ROLE_ADMIN")).willReturn(Optional.of(role));

        Role byId = userService.findByRoleName("ROLE_ADMIN");

        assertThat(byId).isEqualTo(role);
    }
}