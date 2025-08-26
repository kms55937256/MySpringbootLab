package com.rookies4.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
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
     * 1:1 양방향 매핑(비주인)
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

    /**
     * 편의 메서드: 양방향 연관관계 세팅
     * (Lombok @Setter가 생성하는 setter 대신 이 메서드로 설정하는 것을 권장)
     */
    public void setDetail(BookDetail detail) {
        this.detail = detail;
        if (detail != null && detail.getBook() != this) {
            detail.setBook(this);
        }
    }
}