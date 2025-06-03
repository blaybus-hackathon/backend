package com.balybus.galaxy.chat.domain.tblChatMsg;

import com.balybus.galaxy.chat.domain.tblChatRoom.TblChatRoom;
import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.member.domain.TblUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TblChatMsg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Comment(value="채팅방 구분자")
    @JoinColumn(name="cr_seq", nullable = false)
    private TblChatRoom chatRoom;     //채팅방 구분자

    @ManyToOne
    @Comment(value="송신자 구분자")
    @JoinColumn(name="sender_seq", nullable = false)
    private TblUser sender;     //송신자 구분자

    @Comment(value="채팅 내용")
    private String content;

    @Comment(value="읽음 여부")
    private boolean readYn;

    /* ==================================================
     * UPDATE
     * ================================================== */

    public void chRead(){
        this.readYn = true;
    }

}
