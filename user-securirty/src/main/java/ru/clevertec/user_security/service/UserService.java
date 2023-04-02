package ru.clevertec.user_security.service;

import ru.clevertec.user_security.model.Role;
import ru.clevertec.user_security.model.User;

public interface UserService {

    void save(User user);

    User findByUsername(String userName);

    User findById(Long id);

    void delete(Long id);

    Role findByRoleName(String roleName);
}
