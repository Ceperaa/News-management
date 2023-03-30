package ru.clevertec.newsManagement.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsRequestParamDto {

    @Positive
    Long id;

    @Size(min = 2, max = 50)
    String title;

    @Size(min = 2, max = 300)
    String text;

    @Size(min = 2, max = 30)
    String username;

    @Positive
    Integer page;

    @Positive
    Integer size;

}
