package ru.clevertec.newsManagement.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsCreateDto {

    @NotBlank
    @Size(min = 2, max = 30)
    String title;

    @NotBlank
    @Size(min = 2, max = 100)
    String text;

}
