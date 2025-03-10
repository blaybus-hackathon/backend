package com.balybus.galaxy.centermanager.entity;

import com.balybus.galaxy.kakao.model.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tbl_center_manager")
public class CenterManagerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String managerName;
    private String managerPosition;

    @Column(name = "center_id")
    private Long centerId;
}