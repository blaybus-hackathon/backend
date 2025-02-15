package com.balybus.galaxy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // 1xxx
    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    // 2xxx 성공코드가 2xx로 사용되므로 에러코드로 사용하지 않겠습니다.

    // 3xxx 요양 보호사
    NOT_FOUND_HELPER(3000, "요양 보호사 테이블을 찾을 수 없습니다."),
    SIGNUP_HELPER_WORK_TIME_INFO_NULL(3001, "요양 보호사 근무 시간 회원 가입 정보를 다시 확인해주세요"),

    DATA_VALIDATION_ERROR(3002, "데이터 무결성 위반: 필수값을 확인하세요."),
    TRANSACTIONAL_ERROR(3003, "트랜잭션 처리 중 오류가 발생했습니다."),

    SIGNUP_HELPER_EXPERIENCE_INFO_NULL(3004, "요양 보호사 경력 회원 가입 정보를 다시 확인해주세요."),

    HELPER_ALREADY_HAS_WORK_TIME(3005, "해당 요양 보호사는 이미 등록된 근무 시간이 있습니다."),
    HELPER_ALREADY_HAS_EXPERIENCE(3006, "해당 요양 보호사는 이미 등록된 근무 경력이 있습니다."),

    NO_ADDRESS_INFO(3007, "유효한 근무 가능 지역 조합이 없습니다."),
    INVALID_ADDR_INFO(3008, "잘못된 주소 목록"),

    DUPLICATE_WORK_TIME(3009, "중복된 근무 가능 시간 주소가 존재합니다."),

    // 4xxx 로그인
    LOGIN_ID_EXIST(4000, "로그인 아이디가 이미 존재합니다."),
    SIGNUP_INFO_NULL(4001, "회원 가입 정보를 확인해주세요."),
    LOGIN_FAIL(4002, "아이디/비밀번호를 확인해주세요."),
    CENTER_NOT_FOUND(4003, "센터 정보를 찾을 수 없습니다."),

    // 5xxx 서버에러
    INTERNAL_SEVER_ERROR(5000, "서버에서 에러가 발생하였습니다."),

    // 6xxx 파일 업로드
    UPLOAD_FAILED(6000, "파일 업로드에 실패하였습니다."),
    FILE_NOT_FOUND(6001, "첨부파일을 찾을 수 없습니다."),
    ;


    private final int code;
    private final String message;
}
