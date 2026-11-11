package com.inventory.ManagementSystem.repository;

import com.inventory.ManagementSystem.model.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface PasswordResetOtpRepo extends JpaRepository<PasswordResetOtp, Long> {

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Optional<PasswordResetOtp> findByEmail(String email);
}
