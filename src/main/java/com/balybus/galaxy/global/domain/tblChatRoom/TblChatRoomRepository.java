package com.balybus.galaxy.global.domain.tblChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblChatRoomRepository extends JpaRepository<TblChatRoom, Long> {
    Optional<TblChatRoom> findByUserA_IdAndUserB_IdAndPatientLog_Id(Long userA, Long userB, Long patientLogId);

    @Query(value = """
            select
                tcr.id,
                case
                    when tua.id = :userId then tcr.userB.id
                    else tcr.userA.id
                end,
                case
                    when tua.id = :userId then
                        (case when tub.userAuth = 'MEMBER' then concat(thb.name, ' 요양보호사') else concat(tcmb.cmName, ' 관리자') end)
                    else
                        (case when tua.userAuth = 'MEMBER' then concat(tha.name, ' 요양보호사') else concat(tcma.cmName, ' 관리자') end)
                end,
                tcr.patientLog.id,
                tpl.name
            from TblChatRoom tcr
            join TblUser tua on tua = tcr.userA
            left join TblHelper tha on tha.user = tcr.userA
            left join TblCenterManager tcma on tcma.member = tcr.userA
            join TblUser tub on tub = tcr.userB
            left join TblHelper thb on thb.user = tcr.userB
            left join TblCenterManager tcmb on tcmb.member = tcr.userB
            join TblPatientLog tpl on tpl = tcr.patientLog
            where (tcr.userA.id = :userId or tcr.userB.id = :userId)
            """)
    List<Object[]> findObjectList(@Param("userId") Long userId);


    @Query(value = """
            select tcr
            from TblChatRoom tcr
            where tcr.id = :chatRoomId
            and (tcr.userA.id = :userId or tcr.userB.id = :userId)
            """)
    Optional<TblChatRoom> findByIdAndOrUser(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);
}
