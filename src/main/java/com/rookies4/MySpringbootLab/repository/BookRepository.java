package com.rookies4.MySpringbootLab.repository;

import com.rookies4.MySpringbootLab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // ISBN으로 단건 조회
    Optional<Book> findByIsbn(String isbn);

    // 저자명으로 목록 조회
    List<Book> findByAuthor(String author);

    // 제목 일부 포함 검색 (대소문자 무시)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // ISBN 중복 여부 확인
    boolean existsByIsbn(String isbn);

    // Book + BookDetail (1:1 관계) 함께 조회 - ID 기준
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.detail WHERE b.id = :id")
    Optional<Book> findByIdWithDetail(@Param("id") Long id);

    // Book + BookDetail (1:1 관계) 함께 조회 - ISBN 기준
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.detail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithDetail(@Param("isbn") String isbn);

    // 모든 Book + BookDetail 조회 (목록용)
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.detail")
    List<Book> findAllWithDetail();
}