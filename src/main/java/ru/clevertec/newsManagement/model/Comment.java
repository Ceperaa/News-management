package ru.clevertec.newsManagement.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.newsManagement.util.LocalDateStringConvert;
import ru.clevertec.newsManagement.util.StringLocalDateConvert;

import java.time.LocalTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(converter = LocalDateStringConvert.class)
    @JsonDeserialize(converter = StringLocalDateConvert.class)
    private LocalTime time;
    private String text;
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    private News news;

    @PrePersist
    public void setCreateDate() {
        this.time = LocalTime.now();
    }
}
