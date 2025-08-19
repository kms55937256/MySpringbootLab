package com.rookies4.MySpringbootLab.exception.advice;

import com.rookies4.MySpringbootLab.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class DefaultExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(BusinessException e, WebRequest request) {
        log.error("BusinessException 발생: {}", e.getMessage(), e);

        if (isApiRequest(request)) {
            ErrorObject errorObject = new ErrorObject();
            errorObject.setStatusCode(e.getHttpStatus().value());
            errorObject.setMessage(e.getMessage());
            return new ResponseEntity<>(errorObject, e.getHttpStatus());
        } else {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("error/500"); // resources/templates/error/500.html 필요
            mv.addObject("error", e);
            return mv;
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e, WebRequest request) {
        log.error("RuntimeException 발생: {}", e.getMessage(), e);

        if (isApiRequest(request)) {
            ErrorObject errorObject = new ErrorObject();
            errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorObject.setMessage(e.getMessage());
            return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("error/500");
            mv.addObject("error", e);
            return mv;
        }
    }

    // API 요청 여부 판단
    private boolean isApiRequest(WebRequest request) {
        String path = request.getDescription(false);
        if (path != null && path.startsWith("uri=/api/")) {
            return true;
        }
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }
}