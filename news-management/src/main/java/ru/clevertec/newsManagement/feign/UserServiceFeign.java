package ru.clevertec.newsManagement.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsManagement.model.Role;
import ru.clevertec.newsManagement.model.User;

import java.util.Optional;

@FeignClient(value = "security",url = "http://springbootApp82:8002/")
@Component
public interface UserServiceFeign {

    @GetMapping("/user")
    User findByUsername(@RequestParam("username") String username);

    @PostMapping("/user")
    void save(@RequestBody User user);

    @GetMapping("/user/id")
    User findDtoById(@RequestParam("id") Long id);

    @DeleteMapping("/user")
    void delete(@RequestParam("id") Long id);

    @GetMapping("/user/role")
    Role findByRole(@RequestParam("roleName") String roleName);
}
