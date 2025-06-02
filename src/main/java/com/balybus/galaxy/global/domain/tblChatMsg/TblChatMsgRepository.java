package com.balybus.galaxy.global.domain.tblChatMsg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblChatMsgRepository extends JpaRepository<TblChatMsg, Long> {
    Page<TblChatMsg> findByChatRoom_Id(Long chatRoomId, Pageable page);
    Optional<TblChatMsg> findTop1ByChatRoom_IdOrderByIdDesc(Long chatRoomId);
    Long countByChatRoomIdAndSenderIdAndReadYn(Long chatRoomId, Long senderId, boolean readYn);
    List<TblChatMsg> findByChatRoomIdAndSenderIdAndReadYn(Long chatRoomId, Long senderId, boolean readYn);
}
