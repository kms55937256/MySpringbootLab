package com.rookies4.MySpringbootLab.repository;

import com.rookies4.MySpringbootLab.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {

    // Book PK로 상세 조회
    Optional<BookDetail> findByBookId(Long bookId);

    // 상세 + 원본 Book까지 한 번에 로드 (페치 조인)
    @Query("SELECT d FROM BookDetail d JOIN FETCH d.book WHERE d.id = :id")
    Optional<BookDetail> findByIdWithBook(@Param("id") Long bookDetailId);

    // 출판사로 상세 검색 (예시)
    List<BookDetail> findByPublisher(String publisher);
}