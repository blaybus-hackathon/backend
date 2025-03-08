package com.balybus.galaxy.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblChatMsgRepository extends JpaRepository<TblChatMsg, Long> {
    List<TblChatMsg> findBySenderAndReceiver(String sender, String receiver);
}
