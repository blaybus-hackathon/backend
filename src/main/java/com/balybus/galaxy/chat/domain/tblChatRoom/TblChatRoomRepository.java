package com.balybus.galaxy.chat.domain.tblChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TblChatRoomRepository extends JpaRepository<TblChatRoom, Long> {
    Optional<TblChatRoom> findByUserA_IdAndUserB_IdAndPatientLog_Id(Long userA, Long userB, Long patientLogId);
}
