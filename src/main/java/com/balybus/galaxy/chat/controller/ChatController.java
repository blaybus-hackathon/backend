package com.balybus.galaxy.chat.controller;

import com.balybus.galaxy.chat.service.ChatServiceImpl;
import com.balybus.galaxy.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatServiceImpl chatService;
    /**
     * 채팅 리스트 조회
     * @return ResponseEntity
     */
    @GetMapping("/find-list")
    @Operation(summary = "채팅 리스트 조회 API", description = "채팅방을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자정의코드:가입된 이메일",
                    content = @Content(schema = @Schema(implementation = MemberResponse.FindEmail.class))),
            @ApiResponse(responseCode = "404", description = "사용자정의에러코드:미가입된 이메일",
                    content = @Content(schema = @Schema(implementation = MemberResponse.FindEmail.class)))
    })
    public ResponseEntity<?> findList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(chatService.findList(userDetails.getUsername()));
    }
}
