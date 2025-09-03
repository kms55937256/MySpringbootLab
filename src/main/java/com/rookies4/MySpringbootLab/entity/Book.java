package com.rookies4.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 80)
    private String author;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false)
    private Integer price;

    private LocalDate publishDate;

    /*
     * 1:1 양방향 매핑 (비주인)
     * - 주인(Owner): BookDetail (FK: book_id)
     * - Book에서 BookDetail을 참조할 수 있도록 mappedBy="book"
     * - 연관 엔티티 생명주기 관리(cascade=ALL, orphanRemoval=true)
     * - 지연 로딩 권장
     */
    @OneToOne(mappedBy = "book",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private BookDetail detail;

    /*
     * N:1 매핑 (Book -> Publisher)
     * - 여러 책이 하나의 출판사에 속한다.
     * - 외래 키: books.publisher_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    /**
     * BookDetail 편의 메서드 (양방향 연관관계 세팅)
     */
    public void setDetail(BookDetail detail) {
        this.detail = detail;
        if (detail != null && detail.getBook() != this) {
            detail.setBook(this);
        }
    }

    /**
     * Publisher 편의 메서드 (양방향 연관관계 세팅)
     */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
        if (publisher != null && !publisher.getBooks().contains(this)) {
            publisher.getBooks().add(this);
        }
    }
}