package com.github.hjgf0624.sideproject.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // AUTH (인증/인가)
    AUTH_001(401, "AUTH_001", "이메일 또는 비밀번호가 올바르지 않습니다."),
    AUTH_002(409, "AUTH_002", "이미 가입된 이메일입니다."),
    AUTH_003(400, "AUTH_003", "인증 코드가 일치하지 않습니다."),
    AUTH_004(410, "AUTH_004", "인증 코드가 만료되었습니다."),
    AUTH_005(404, "AUTH_005", "존재하지 않는 사용자입니다."),
    AUTH_006(401, "AUTH_006", "유효하지 않은 토큰입니다."),
    AUTH_007(403, "AUTH_007", "접근 권한이 없습니다."),
    AUTH_008(400, "AUTH_008", "비밀번호 재설정에 실패했습니다."),
    AUTH_009(500, "AUTH_009", "FCM 토큰 저장에 실패했습니다."),
    AUTH_010(400, "AUTH_010", "유효하지 않은 이메일 형식입니다."),
    AUTH_011(400, "AUTH_011", "생년월일 형식이 올바르지 않습니다."),
    AUTH_012(400, "AUTH_012", "비밀번호가 일치하지 않습니다."),
    AUTH_013(400, "AUTH_013", "FCM 토큰이 전달되지 않았습니다."),
    AUTH_014(400, "AUTH_014", "액세스 토큰이 아직 만료되지 않았습니다."),
    AUTH_015(401, "AUTH_015", "리프레시 토큰이 유효하지 않습니다."),
    AUTH_016(410, "AUTH_016", "리프레시 토큰이 만료되었습니다."),
    AUTH_017(500, "AUTH_017", "유저 삭제 중 오류가 발생했습니다."),

    // MSG (메시지)
    MSG_001(404, "MSG_001", "메시지를 찾을 수 없습니다."),
    MSG_002(500, "MSG_002", "메시지 작성에 실패했습니다."),
    MSG_003(400, "MSG_003", "참여 가능한 메시지가 아닙니다."),
    MSG_004(409, "MSG_004", "이미 참여한 메시지입니다."),
    MSG_005(404, "MSG_005", "카테고리를 찾을 수 없습니다."),
    MSG_006(400, "MSG_006", "날짜 형식이 잘못되었습니다."),
    MSG_007(500, "MSG_007", "메시지 참여 중 오류가 발생했습니다."),

    // ALARM (알림 / 브로드캐스트)
    ALARM_001(500, "ALARM_001", "주변 알람을 불러오는 데 실패했습니다."),
    ALARM_002(400, "ALARM_002", "위치 정보가 유효하지 않습니다."),
    ALARM_003(404, "ALARM_003", "브로드캐스트 대상 사용자가 없습니다."),
    ALARM_004(500, "ALARM_004", "알림 전송에 실패했습니다."),

    CATEGORY_NOT_FOUND(404, "CATEGORY_001", "해당 카테고리를 찾을 수 없습니다."),
    EMAIL_SEND_FAILED(500, "EMAIL_001", "이메일 전송에 실패했습니다."),

    FCM_NO_TOKENS(400,"FCM_001", "전송할 FCM 토큰이 존재하지 않습니다."),
    FCM_SEND_FAILED(500, "FCM_002", "FCM 메시지 전송 중 오류가 발생했습니다."),

    SMS_SEND_FAILED(500, "SMS_001", "SMS 인증번호 전송에 실패했습니다."),

    // ROLE (권한)
    ROLE_001(403, "ROLE_001", "요청한 리소스에 대한 권한이 없습니다."),
    ROLE_002(400, "ROLE_002", "유효하지 않은 권한 타입입니다."),
    ROLE_003(409, "ROLE_003", "이미 권한이 부여된 사용자입니다."),
    ROLE_004(404, "ROLE_004", "해당 권한 정보를 찾을 수 없습니다.");


    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}