package com.example.eurekaclient.service.impl;

import com.example.eurekaclient.exception.EntityNotFoundException;
import com.example.eurekaclient.model.Role;
import com.example.eurekaclient.model.User;
import com.example.eurekaclient.repositiry.RoleRepository;
import com.example.eurekaclient.repositiry.UserRepository;
import com.example.eurekaclient.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
