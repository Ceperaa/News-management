package ru.clevertec.newsManagement.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequestParamDto {

    @Positive
    @Min(1)
    Long id;

    @Size(min = 2, max = 300)
    String text;

    @Size(min = 2, max = 30)
    String username;

    @Positive
    Integer page;

    @Positive
    Integer size;
}
