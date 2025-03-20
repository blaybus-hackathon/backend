package com.balybus.galaxy.domain.tblMatching;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchState {
    INIT(0, "매칭 요청 전"),
    MATCH_REQUEST(1, "매칭 요청"),
    PERMIT_TUNE(2, "수락함(조율중)"),
    MATCH_FIN(3, "매칭 완료"),
    REJECT(4, "응답 거절");

    private final int state;
    private final String msg;
}