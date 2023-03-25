package ru.clevertec.newsManagement.model.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreateDto {

    @Size(min = 2, max = 100)
    String text;

    @Size(min = 2, max = 30)
    String username;

    Long newsId;
}
