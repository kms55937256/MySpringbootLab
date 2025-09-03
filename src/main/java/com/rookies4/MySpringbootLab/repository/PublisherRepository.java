package com.rookies4.MySpringbootLab.repository;

import com.rookies4.MySpringbootLab.entity.Publisher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    // 방법 1: EntityGraph (책 목록 fetch join)
    @EntityGraph(attributePaths = "books")
    Optional<Publisher> findById(@Param("id") Long id);

    // 방법 2: JPQL로 직접 fetch join (더 명시적)
    @Query("SELECT p FROM Publisher p LEFT JOIN FETCH p.books WHERE p.id = :id")
    Optional<Publisher> findByIdWithBooks(@Param("id") Long id);

    boolean existsByName(String name);
}