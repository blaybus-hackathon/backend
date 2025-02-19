//package com.balybus.galaxy.notice.Contoroller;
//
//import com.balybus.galaxy.notice.dto.request.NoticeDto;
//import com.balybus.galaxy.notice.serviceImpl.NoticeService;
//import com.balybus.galaxy.patient.dto.request.TblPatientLogDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/notice")
//@RequiredArgsConstructor
//public class NoticeController {
//
//    private final NoticeService noticeService;
//
//    /* 공지사항 등록 */
//    @PostMapping("/up")
//    public ResponseEntity<NoticeDto> noticeUp(@RequestBody TblPatientLogDTO tblPatientLogDTO) {
//        NoticeDto response = noticeService.saveNotice(tblPatientLogDTO);
//        return ResponseEntity.ok(response);
//    }
//
//    /* 공지사항 수정 */
//    @PostMapping("/re/{id}")
//    public ResponseEntity<NoticeDto> reNotice(@PathVariable Long id, @RequestBody TblPatientLogDTO tblPatientLogDTO) {
//        NoticeDto response = noticeService.updateNotice(tblPatientLogDTO);
//        return ResponseEntity.ok(response);
//    }
//}
