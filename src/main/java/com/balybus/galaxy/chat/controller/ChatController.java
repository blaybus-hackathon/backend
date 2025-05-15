package com.balybus.galaxy.chat.controller;

import com.balybus.galaxy.chat.dto.ChatMsgRequestDto;
import com.balybus.galaxy.chat.dto.ChatMsgResponseDto;
import com.balybus.galaxy.chat.dto.ChatRoomResponseDto;
import com.balybus.galaxy.chat.service.ChatServiceImpl;
import com.balybus.galaxy.global.exception.ErrorResponse;
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
    @Operation(summary = "채팅 리스트 조회 API", description = "채팅방 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자정의코드:채팅방 리스트 조회 완료",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponseDto.FindList.class))),
            @ApiResponse(responseCode = "3010", description = "사용자정의에러코드:로그인한 회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> findList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(chatService.findList(userDetails.getUsername()));
    }

    /**
     * 채팅 내역 조회
     * @return ResponseEntity
     */
    @GetMapping("/find-detail")
    @Operation(summary = "채팅 내역 조회 API", description = "채팅방별 채팅내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자정의코드:채팅 내역 조회 완료",
                    content = @Content(schema = @Schema(implementation = ChatMsgResponseDto.FindChatDetail.class))),
            @ApiResponse(responseCode = "3010", description = "사용자정의에러코드:로그인한 회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1000", description = "사용자정의에러코드:채팅 상대 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "9004", description = "사용자정의에러코드:채팅방 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> findChatDetail(@AuthenticationPrincipal UserDetails userDetails,
                                            ChatMsgRequestDto.FindChatDetail dto) {
        return ResponseEntity.ok().body(chatService.findChatDetail(dto, userDetails.getUsername()));
    }
}
