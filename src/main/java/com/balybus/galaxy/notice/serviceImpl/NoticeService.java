package com.balybus.galaxy.notice.serviceImpl;

import com.balybus.galaxy.notice.dto.NoticeDto;

public interface NoticeService {
    NoticeDto saveNotice(NoticeDto noticeDto);
    NoticeDto updateNotice(NoticeDto noticeDto);
}
