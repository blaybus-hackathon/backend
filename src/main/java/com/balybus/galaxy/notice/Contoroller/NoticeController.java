package com.balybus.galaxy.notice.Contoroller;

import com.balybus.galaxy.notice.dto.NoticeDto;
import com.balybus.galaxy.notice.serviceImpl.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /* 공지사항 등록 */
    @PostMapping("/up")
    public ResponseEntity<NoticeDto> noticeUp(@RequestBody NoticeDto noticeDto) {
        return ResponseEntity.ok(noticeService.saveNotice(noticeDto));
    }

    /* 공지사항 수정 */
    @PostMapping("/re")
    public ResponseEntity<NoticeDto> reNotice(@RequestBody NoticeDto noticeDto) {
        return ResponseEntity.ok(noticeService.updateNotice(noticeDto));
    }
}
