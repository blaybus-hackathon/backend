package com.balybus.galaxy.member.repository;


import com.balybus.galaxy.member.domain.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<TblUser, Long> {
    Optional<TblUser> findByEmail(String email);
}
