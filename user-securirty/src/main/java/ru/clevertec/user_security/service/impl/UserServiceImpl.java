package ru.clevertec.user_security.service.impl;

import ru.clevertec.user_security.exception.EntityNotFoundException;
import ru.clevertec.user_security.model.Role;
import ru.clevertec.user_security.model.User;
import ru.clevertec.user_security.repositiry.RoleRepository;
import ru.clevertec.user_security.repositiry.UserRepository;
import ru.clevertec.user_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public User findByUsername(String userName) {
        return repository.findByUsername(userName)
                .orElseThrow(() -> new EntityNotFoundException(User.class));
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRole(roleName)
                .orElseThrow(() -> new EntityNotFoundException(Role.class));
    }
}
