package ru.clevertec.newsManagement.model.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsUpdateDto {

    @Size(min = 2, max = 30)
    String title;

    @Size(min = 2, max = 100)
    String text;
}
