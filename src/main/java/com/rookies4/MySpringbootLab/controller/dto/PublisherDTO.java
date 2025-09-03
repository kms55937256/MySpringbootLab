package com.rookies4.MySpringbootLab.controller.dto;

import com.rookies4.MySpringbootLab.entity.Publisher;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class PublisherDTO {

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        private String name;
        private LocalDate establishedDate;
        private String address;

        // DTO → Entity 변환
        public Publisher toEntity() {
            return Publisher.builder()
                    .name(this.name)
                    .establishedDate(this.establishedDate)
                    .address(this.address)
                    .books(new ArrayList<>()) // ✅ Null 방지 (항상 빈 리스트로 초기화)
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String name;
        private LocalDate establishedDate;
        private String address;

        public static Response from(Publisher publisher) {
            return Response.builder()
                    .id(publisher.getId())
                    .name(publisher.getName())
                    .establishedDate(publisher.getEstablishedDate())
                    .address(publisher.getAddress())
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SimpleResponse {
        private Long id;
        private String name;
        private long bookCount;

        public static SimpleResponse fromEntityWithCount(Publisher publisher) {
            return SimpleResponse.builder()
                    .id(publisher.getId())
                    .name(publisher.getName())
                    .bookCount(
                            publisher.getBooks() == null ? 0 : publisher.getBooks().size() // ✅ Null 안전 처리
                    )
                    .build();
        }
    }
}