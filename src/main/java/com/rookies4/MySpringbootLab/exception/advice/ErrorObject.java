package com.rookies4.MySpringbootLab.exception.advice;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private String timestamp;

    // timestamp는 getter에서 동적으로 생성
    public String getTimestamp() {
        LocalDateTime ldt = LocalDateTime.now();
        return DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss E a",
                Locale.KOREA
        ).format(ldt);
    }
}