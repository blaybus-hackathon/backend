package com.balybus.galaxy.matching;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/match")
public class MatchingController {
    // 매칭 요청 전/ 메칭 요청 / 수락함(조율중) / 매칭 완료 / 거절
}
