package com.rookies4.MySpringbootLab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final HttpStatus httpStatus;

    public BusinessException(String message) {
        this(message, HttpStatus.NOT_FOUND); // 기본 404 권장
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);              // ★ RuntimeException 메시지 세팅
        this.httpStatus = httpStatus;
    }
}