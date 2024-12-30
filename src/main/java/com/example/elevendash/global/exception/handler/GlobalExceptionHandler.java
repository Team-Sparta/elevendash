package com.example.elevendash.global.exception.handler;

import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import com.example.elevendash.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse<String>> handle(BaseException e) {
        return CommonResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse<String>> handle(RuntimeException e) {
        log.error("RuntimeException : {}", e.getMessage());
        return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage());
        return CommonResponse.fail(ErrorCode.VALIDATION_ERROR, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        return CommonResponse.fail(ErrorCode.UNAUTHORIZED_ACCESS, Objects.requireNonNull(ex.getMessage()));
    }
}