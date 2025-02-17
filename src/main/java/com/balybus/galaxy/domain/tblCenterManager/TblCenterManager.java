package com.balybus.galaxy.domain.tblCenterManager;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.domain.tblCenter.TblCenter;
import com.balybus.galaxy.domain.tblImg.TblImg;
import com.balybus.galaxy.global.utils.file.ChangeProfileImg;
import com.balybus.galaxy.member.domain.TblUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TblCenterManager extends BaseEntity implements ChangeProfileImg {

    @Id
    @Column(name="cm_seq")
    @Comment(value="관리자 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //구분자

    @OneToOne
    @Comment(value="유저 구분자")
    @JoinColumn(name="user_seq", nullable = false)
    private TblUser member;      //유저 구분자

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment(value="센터 구분자")
    @JoinColumn(name = "center_seq", nullable = false)
    private TblCenter center;   //센터 구분자

    @OneToOne(fetch = FetchType.LAZY)
    @Comment("이미지 구분자")
    @JoinColumn(name = "img_seq")
    private TblImg img;

    @Comment(value="직책")
    @Column(length = 10, nullable = false)
    private String cmPosition;  // 직책

    @Comment(value="직원명")
    @Column(length = 10, nullable = false)
    private String cmName;      // 직원명

    /* ==================================================
     * UPDATE
     * ================================================== */
    @Override
    public TblImg getImg() { return this.img; }

    @Override
    public void updateImg(TblImg img) { this.img = img; }
}
