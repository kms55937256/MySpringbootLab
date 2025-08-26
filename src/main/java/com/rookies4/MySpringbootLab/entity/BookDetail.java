package com.rookies4.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_details")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BookDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_detail_id")
    private Long id;

    // 상세 정보 필드들 (필수 아님: 요구사항상 선택 입력 가능)
    @Column
    private String description;

    @Column
    private String language;

    @Column
    private Integer pageCount;

    @Column
    private String publisher;

    @Column
    private String coverImageUrl;

    @Column
    private String edition;

    /*
     * 1:1 양방향 매핑(주인, FK 보유)
     * - FK 컬럼: book_id (유니크)
     * - 지연 로딩 권장
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;

    /**
     * 편의 메서드: 양방향 연관관계 세팅
     */
    public void setBook(Book book) {
        this.book = book;
        if (book != null && book.getDetail() != this) {
            book.setDetail(this);
        }
    }
}