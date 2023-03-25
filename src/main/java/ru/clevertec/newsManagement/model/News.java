package ru.clevertec.newsManagement.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import ru.clevertec.newsManagement.util.LocalDateStringConvert;
import ru.clevertec.newsManagement.util.StringLocalDateConvert;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(converter = LocalDateStringConvert.class)
    @JsonDeserialize(converter = StringLocalDateConvert.class)
    private LocalTime time;
    private String text;
    private String title;
    private String username;

    @ToString.Exclude
    @OneToMany(mappedBy = "news")
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void setCreateDate() {
        this.time = LocalTime.now();
    }
}
