package com.rookies4.MySpringbootLab.controller.dto;

import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.entity.BookDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    /* ========== Create(POST) 요청 DTO ========== */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
        private String isbn;

        @NotNull(message = "가격은 필수 입력 항목입니다.")
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        private LocalDate publishDate;

        /** 🔽 새로 추가 */
        @NotNull(message = "출판사 ID는 필수입니다.")
        private Long publisherId;

        @Valid
        private BookDetailRequest detailRequest; // 선택

        public Book toEntity() {
            return Book.builder()
                    .title(title)
                    .author(author)
                    .isbn(isbn)
                    .price(price)
                    .publishDate(publishDate)
                    .build();
            // publisher는 서비스단에서 publisherId로 조회 후 setPublisher() 해줌
        }
    }

    /* ========== Update(PUT) 요청 DTO ========== */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookUpdateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;

        @NotNull(message = "가격은 필수 입력 항목입니다.")
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        private LocalDate publishDate;

        /** 🔽 새로 추가 */
        @NotNull(message = "출판사 ID는 필수입니다.")
        private Long publisherId;

        @Valid
        private BookDetailRequest detailRequest; // 선택
    }

    /* ========== Book 부분수정(PATCH) 요청 DTO(모두 선택) ========== */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PatchRequest {
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private Long publisherId; // 🔽 PATCH라 optional로 둠
    }

    /* ========== BookDetail 부분수정(PATCH) 요청 DTO(모두 선택) ========== */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailPatchRequest {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    /* ========== 상세 요청 DTO(POST/PUT에서 사용) ========== */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailRequest {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;

        public BookDetail toEntity() {
            return BookDetail.builder()
                    .description(description)
                    .language(language)
                    .pageCount(pageCount)
                    .publisher(publisher)
                    .coverImageUrl(coverImageUrl)
                    .edition(edition)
                    .build();
        }
    }

    /* ========== 응답 DTOs ========== */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail; // 포함 응답
        private Long publisherId;          // 🔽 응답에도 publisherId 추가 가능

        public static BookResponse from(Book book) {
            BookDetailResponse detailDto = null;
            if (book.getDetail() != null) {
                BookDetail d = book.getDetail();
                detailDto = BookDetailResponse.builder()
                        .id(d.getId())
                        .description(d.getDescription())
                        .language(d.getLanguage())
                        .pageCount(d.getPageCount())
                        .publisher(d.getPublisher())
                        .coverImageUrl(d.getCoverImageUrl())
                        .edition(d.getEdition())
                        .build();
            }
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailDto)
                    .publisherId(book.getPublisher() != null ? book.getPublisher().getId() : null)
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}