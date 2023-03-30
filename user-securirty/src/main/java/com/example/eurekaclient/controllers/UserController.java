package com.example.eurekaclient.controllers;

import com.example.eurekaclient.model.Role;
import com.example.eurekaclient.model.User;
import com.example.eurekaclient.repositiry.RoleRepository;
import com.example.eurekaclient.repositiry.UserRepository;

import com.example.eurekaclient.service.UserService;
import feign.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> findByUsername(@Param String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PostMapping
    public void save(@RequestBody User user) {
        userService.save(user);
    }

    @GetMapping("/id")
    public ResponseEntity<User> findDtoById(@Param Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping
    void delete(@Param Long id){
        userService.delete(id);
    }

    @GetMapping("/role")
    Role findByRole(@Param String roleName){
       return userService.findByRoleName(roleName);
    }
}
