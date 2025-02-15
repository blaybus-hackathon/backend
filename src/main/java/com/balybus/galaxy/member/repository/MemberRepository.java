package com.balybus.galaxy.member.repository;


import com.balybus.galaxy.member.domain.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<TblUser, Long> {
    Optional<TblUser> findByEmail(String email);
}
