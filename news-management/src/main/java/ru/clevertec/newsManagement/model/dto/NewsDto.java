package ru.clevertec.newsManagement.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.clevertec.newsManagement.util.LocalDateStringConvert;
import ru.clevertec.newsManagement.util.StringLocalDateConvert;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsDto {

    Long id;
    String title;
    String text;

    @JsonSerialize(converter = LocalDateStringConvert.class)
    @JsonDeserialize(converter = StringLocalDateConvert.class)
    LocalTime time;

    String username;
    ArrayList<CommentDto> comments = new ArrayList<>();
}
