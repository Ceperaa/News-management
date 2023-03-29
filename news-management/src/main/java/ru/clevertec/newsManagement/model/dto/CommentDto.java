package ru.clevertec.newsManagement.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.clevertec.newsManagement.util.LocalDateStringConvert;
import ru.clevertec.newsManagement.util.StringLocalDateConvert;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    Long id;
    String text;

    @JsonSerialize(converter = LocalDateStringConvert.class)
    @JsonDeserialize(converter = StringLocalDateConvert.class)
    LocalTime time;

    String username;
    Long newsId;
}
