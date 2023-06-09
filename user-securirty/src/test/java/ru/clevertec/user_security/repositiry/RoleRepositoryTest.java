package ru.clevertec.user_security.repositiry;

import ru.clevertec.user_security.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RoleRepositoryTest extends TestRepository {

    @Autowired
    private RoleRepository roleRepository;

    @Container
    protected static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:14.7");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", CONTAINER::getPassword);
        registry.add("spring.datasource.username", CONTAINER::getUsername);
    }

    @Test
    void findByRole() {
        Optional<Role> roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
        assertThat(roleAdmin.get().getRole()).isEqualTo("ROLE_ADMIN");
    }
}