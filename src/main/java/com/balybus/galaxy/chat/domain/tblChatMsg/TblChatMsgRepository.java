package com.balybus.galaxy.chat.domain.tblChatMsg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblChatMsgRepository extends JpaRepository<TblChatMsg, Long> {
    Page<TblChatMsg> findByChatRoom_Id(Long chatRoomId, Pageable page);
}
