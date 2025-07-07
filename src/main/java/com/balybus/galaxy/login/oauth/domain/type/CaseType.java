package com.balybus.galaxy.login.oauth.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CaseType {
    CASE1("CASE1", "첫 번째 케이스"),
    CASE2("CASE2", "두 번째 케이스"),
    CASE3("CASE3"," 세 번째 케이스"),
    CASE4("CASE4", "네 번째 케이스");

    private final String code;
    private final String caseNum;

    public static CaseType of(String code) {
        return Arrays.stream(CaseType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(CASE4);
    }
}
