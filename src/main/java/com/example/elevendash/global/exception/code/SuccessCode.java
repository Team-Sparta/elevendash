package com.example.elevendash.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    SUCCESS(HttpStatus.OK, "성공했습니다."),
    SUCCESS_INSERT(HttpStatus.CREATED, "데이터 저장에 성공했습니다."),
    SUCCESS_DELETE(HttpStatus.OK, "데이터 삭제에 성공했습니다."),
    SUCCESS_UPDATE(HttpStatus.OK, "데이터 수정에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}