package com.rookies4.MySpringbootLab.repository;

import com.rookies4.MySpringbootLab.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")              // ← test 프로필(H2) 사용
@Transactional                      // 기본은 테스트 끝나면 롤백
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    private Book springBoot() {
        return Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();
    }

    private Book jpa() {
        return Book.builder()
                .title("JPA 프로그래밍")
                .author("박둘리")
                .isbn("9788956746432")
                .price(35000)
                .publishDate(LocalDate.of(2025, 4, 30))
                .build();
    }

    @Test
    @DisplayName("도서 등록 테스트 (testCreateBook)")
    void testCreateBook() {
        Book saved = bookRepository.save(springBoot());
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("스프링 부트 입문");
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트 (testFindByIsbn)")
    void testFindByIsbn() {
        bookRepository.save(springBoot());
        bookRepository.save(jpa());

        assertThat(bookRepository.findByIsbn("9788956746425"))
                .isPresent()
                .get().extracting("author").isEqualTo("홍길동");

        assertThat(bookRepository.findByIsbn("NOPE")).isNotPresent();
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트 (testFindByAuthor)")
    void testFindByAuthor() {
        bookRepository.save(springBoot());
        bookRepository.save(jpa());

        assertThat(bookRepository.findByAuthor("홍길동"))
                .hasSize(1)
                .first().extracting("title").isEqualTo("스프링 부트 입문");

        assertThat(bookRepository.findByAuthor("박둘리"))
                .hasSize(1)
                .first().extracting("title").isEqualTo("JPA 프로그래밍");
    }

    @Test
    @DisplayName("도서 정보 수정 테스트 (testUpdateBook)")
    void testUpdateBook() {
        Book saved = bookRepository.save(springBoot());
        saved.setPrice(31000);
        saved.setTitle("스프링 부트 입문(개정판)");

        Book updated = bookRepository.save(saved);

        assertThat(updated.getPrice()).isEqualTo(31000);
        assertThat(updated.getTitle()).isEqualTo("스프링 부트 입문(개정판)");
    }

    @Test
    @DisplayName("도서 삭제 테스트 (testDeleteBook)")
    void testDeleteBook() {
        Long id = bookRepository.save(springBoot()).getId();

        bookRepository.deleteById(id);

        assertThat(bookRepository.findById(id)).isNotPresent();
    }

    // ※ 실제 MariaDB(lab_db)에 남기는 통합 확인이 필요하면 @Rollback(false)만 추가해서 한 번 실행:
    // @Test @Rollback(false) void saveToRealDbOnce() { bookRepository.save(springBoot()); }
}