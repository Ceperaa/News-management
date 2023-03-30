package ru.clevertec.newsManagement.servises;

import ru.clevertec.newsManagement.model.Roles;
import ru.clevertec.newsManagement.model.User;
import ru.clevertec.newsManagement.model.dto.UserAuthDto;

public interface UserService {

    void delete(Long id);

    void registrationUserDto(UserAuthDto registrationUserDto, Roles roles);
}
