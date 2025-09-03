package com.rookies4.MySpringbootLab.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common errors - 공통 에러
    RESOURCE_NOT_FOUND("%s not found with %s: %s", HttpStatus.NOT_FOUND),
    RESOURCE_DUPLICATE("%s already exists with %s: %s", HttpStatus.CONFLICT),
    RESOURCE_ALREADY_EXISTS("%s already exists: %s", HttpStatus.CONFLICT),

    // Student specific errors - 학생 관련
    STUDENT_NUMBER_DUPLICATE("Student already exists with student number: %s", HttpStatus.CONFLICT),

    // StudentDetail specific errors - 학생 상세정보 관련
    EMAIL_DUPLICATE("Student detail already exists with email: %s", HttpStatus.CONFLICT),
    PHONE_NUMBER_DUPLICATE("Student detail already exists with phone number: %s", HttpStatus.CONFLICT),

    // Book specific errors - 도서 관련
    ISBN_DUPLICATE("Book already exists with ISBN: %s", HttpStatus.CONFLICT),

    // Publisher specific errors - 출판사 관련 특수한 경우
    PUBLISHER_DUPLICATE("Publisher already exists with name: %s", HttpStatus.CONFLICT),
    BAD_REQUEST("잘못된 요청입니다: %s", HttpStatus.BAD_REQUEST);

    private final String messageTemplate;
    private final HttpStatus httpStatus;

    public String formatMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}