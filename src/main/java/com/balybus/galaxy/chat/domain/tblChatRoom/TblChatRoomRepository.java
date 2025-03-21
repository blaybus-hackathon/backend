package com.balybus.galaxy.chat.domain.tblChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblChatRoomRepository extends JpaRepository<TblChatRoom, Long> {
}
