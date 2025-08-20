package com.rookies4.MySpringbootLab.controller.dto;

import com.rookies4.MySpringbootLab.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class BookDTO {

    // 📌 등록 요청 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
        private String isbn;

        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        private LocalDate publishDate;

        // DTO → Entity 변환
        public Book toEntity() {
            Book book = new Book();
            book.setTitle(this.title);
            book.setAuthor(this.author);
            book.setIsbn(this.isbn);
            book.setPrice(this.price);
            book.setPublishDate(this.publishDate);
            return book;
        }
    }

    // 📌 수정 요청 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdateRequest {
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;

        private LocalDate publishDate;
    }

    // 📌 응답 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        // Entity → DTO 변환
        public static BookResponse from(Book book) {
            return new BookResponse(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getPrice(),
                    book.getPublishDate()
            );
        }
    }
}