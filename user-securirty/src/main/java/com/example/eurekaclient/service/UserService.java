package com.example.eurekaclient.service;

import com.example.eurekaclient.model.Role;
import com.example.eurekaclient.model.User;

public interface UserService {

    void save(User user);

    User findByUsername(String userName);

    User findById(Long id);

    void delete(Long id);

    Role findByRoleName(String roleName);
}
