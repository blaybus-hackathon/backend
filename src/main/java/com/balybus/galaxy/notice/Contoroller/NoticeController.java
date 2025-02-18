package com.balybus.galaxy.notice.Contoroller;

import com.balybus.galaxy.notice.dto.NoticeDto;
import com.balybus.galaxy.notice.dto.ReNoticeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class NoticeController {

    /*공지사항 등록*/
    @PostMapping("/up/notice")
    public ResponseEntity<NoticeDto> NoticeUp(@RequestBody NoticeDto){
        return ResponseEntity.ok(NoticeUpService(NoticeDto));

    }

    /*공지사항 등록 수정*/
    @PostMapping("/re/notice")
    public ResponseEntity<ReNoticeDto> ReNotice(@RequestBody ReNoticeDto){
        return ResponseEntity.ok(NoticeUpService(ReNoticeDto));
    }


}
