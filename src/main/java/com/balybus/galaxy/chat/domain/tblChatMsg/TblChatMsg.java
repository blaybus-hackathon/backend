package com.balybus.galaxy.chat.domain.tblChatMsg;

import com.balybus.galaxy.chat.domain.tblChatRoom.TblChatRoom;
import com.balybus.galaxy.domain.BaseEntity;
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

    private String content;
}
