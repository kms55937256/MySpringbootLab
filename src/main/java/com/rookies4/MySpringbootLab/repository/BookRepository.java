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

    // 기본 조회
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthor(String author);

    // ISBN 중복 체크
    boolean existsByIsbn(String isbn);

    // Book + BookDetail 을 한 번에 로드 (페치 조인)
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.detail WHERE b.id = :id")
    Optional<Book> findByIdWithDetail(@Param("id") Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.detail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithDetail(@Param("isbn") String isbn);

    // (옵션) 제목 검색 지원 – 컨트롤러의 title 검색용
    List<Book> findByTitleContainingIgnoreCase(String title);
}