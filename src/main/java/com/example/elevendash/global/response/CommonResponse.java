package com.example.elevendash.global.response;

import com.example.elevendash.global.exception.code.ErrorCode;
import com.example.elevendash.global.exception.code.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public record CommonResponse<T>(
        boolean success,
        String message,
        T result) implements Serializable {
    public static <T> ResponseEntity<CommonResponse<T>> success(SuccessCode successCode) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new CommonResponse<>(true, successCode.getMessage(), (T) new CommonEmptyResponse()));
    }

    public static <T> ResponseEntity<CommonResponse<T>> success(SuccessCode successCode, T data) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new CommonResponse<>(true, successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<CommonResponse<T>> fail(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new CommonResponse<>(false, errorCode.getMessage(), (T) new CommonEmptyResponse()));
    }

    public static <T> ResponseEntity<CommonResponse<T>> fail(ErrorCode errorCode, T data) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new CommonResponse<>(false, errorCode.getMessage(), data));
    }

    public static <T> ResponseEntity<CommonResponse<T>> fail(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse<>(false, exception.getMessage(), (T) new CommonEmptyResponse()));
    }
}