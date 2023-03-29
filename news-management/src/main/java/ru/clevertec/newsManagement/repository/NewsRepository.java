package ru.clevertec.newsManagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newsManagement.model.News;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Override
    @EntityGraph(attributePaths = "comments")
    Optional<News> findById(Long id);

    @EntityGraph(attributePaths = "comments")
    Page<News> findAll(Specification<News> example, Pageable page);
}
