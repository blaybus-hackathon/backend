package com.balybus.galaxy.global.domain.tblAuthenticationMail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblAuthenticationMailRepository  extends JpaRepository<TblAuthenticationMail, Long> {
    Optional<TblAuthenticationMail> findByEmail(String email);
    Optional<TblAuthenticationMail> findByIdAndEmailAndCode(Long id, String email, String code);
}
