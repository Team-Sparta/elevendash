package com.example.elevendash.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Server
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사 실패"),
    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "제약 조건 위반"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생하였습니다."),

    /**
     * Common
     */
    NOT_FOUND_ENUM_CONSTANT(HttpStatus.NOT_FOUND, "열거형 상수값을 찾을 수 없습니다."),
    IS_NULL(HttpStatus.BAD_REQUEST, "NULL 값이 들어왔습니다."),
    COMMON_INVALID_PARAM(HttpStatus.BAD_REQUEST, "요청한 값이 올바르지 않습니다."),
    NO_SUCH_METHOD(HttpStatus.BAD_REQUEST, "메소드를 찾을 수 없습니다."),
    INVALID_AUTHENTICATION(HttpStatus.BAD_REQUEST, "인증이 올바르지 않습니다."),

    /**
     * Authentication
     */
    INTERNAL_AUTHENTICATION_SERVICE(HttpStatus.BAD_REQUEST, "인증 서비스가 존재하지 않습니다."),
    NOT_FOUND_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "PROVIDER를 찾을 수 없습니다."),
    NON_EXPIRED_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정이 탈퇴되었습니다."),
    NON_LOCKED_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정이 정지되었습니다."),
    DISABLE_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정은 비활성화 상태입니다."),
    EXPIRED_CREDENTIAL(HttpStatus.BAD_REQUEST, "사용자 인증 정보가 만료되었습니다."),


    /**
     * Json Web Token
     */
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    MALFORMED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 형식의 토큰입니다."),
    SIGNATURE_EXCEPTION(HttpStatus.BAD_REQUEST, "토큰 서명이 올바르지 않습니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다."),


    /**
     * User
     */
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    EXIST_USERNAME(HttpStatus.BAD_REQUEST, "중복된 이름입니다."),
    WRONG_CONDITION_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 하며 8글자 이상이어야 합니다."),
    WRONG_CONDITION_EMAIL(HttpStatus.BAD_REQUEST, "형식에 맞지 않는 이메일입니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호와 새로운 비밀번호가 동일합니다."),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호와 새로운 비밀번호를 모두 입력해주세요."),
    ALREADY_DELETED_USER(HttpStatus.BAD_REQUEST, "이미 삭제된 유저 입니다."),
    NONE_NAME(HttpStatus.BAD_REQUEST, "이름은 필수값입니다."),
    BAD_PROVIDER(HttpStatus.BAD_REQUEST, "소셜로그인은 비밀번호가 없어야하며 providerId가 필수입니다."),
    BAD_EMAIL(HttpStatus.BAD_REQUEST, "이메일을 통한 회원가입은 비밀번호가 필수입니다."),

    /**
     * Image
     */
    S3_UPLOADER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생하였습니다."),
    BAD_FORMAT_IMG(HttpStatus.BAD_REQUEST, "이미지 파일의 형식이 맞지 않습니다."),

    /**
     * Bookmark
     */
    ALREADY_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 북마크하셨습니다"),
    NOT_FOUND_BOOKMARK(HttpStatus.NOT_FOUND, "북마크가 존재하지 않습니다"),
    NOT_CORRECT_INFORMATION(HttpStatus.BAD_REQUEST, "상점과 멤버의 정보가 북마크와 일치하지 않습니다"),

    /**
     * Store
     */
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "상점 정보를 찾을 수 없습니다."),
    NOT_SAME_MEMBER(HttpStatus.NOT_ACCEPTABLE, "상점과 멤버정보가 일치하지 않습니다."),
    NOT_VALID_OPEN_TIME(HttpStatus.BAD_REQUEST, "오픈시간이 마감시간보다 빠릅니다"),
    ENOUGH_STORE(HttpStatus.BAD_REQUEST, "상점의 수가 3개입니다 더이상 추가할 수 없습니다"),
    NOT_OWNER(HttpStatus.NOT_ACCEPTABLE, "OWNER 권한만 할 수 있습니다."),

    /**
     * Menu
     */

    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리 정보를 찾을 수 없습니다."),
    NOT_FOUND_MENU(HttpStatus.NOT_FOUND, "메뉴 정보를 찾을 수 없습니다."),
    NOT_SAME_STORE(HttpStatus.NOT_ACCEPTABLE, "상점과 메뉴정보가 일치하지 않습니다."),
    NOT_FOUND_MENU_OPTION(HttpStatus.NOT_FOUND, "메뉴 옵션 정보를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
