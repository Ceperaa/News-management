package ru.clevertec.newsManagement.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuthDto {

    @NotBlank
    @Size(min = 2, max = 30)
    String username;

    @NotBlank
    @Size(min = 6, max = 30)
    String password;
}
