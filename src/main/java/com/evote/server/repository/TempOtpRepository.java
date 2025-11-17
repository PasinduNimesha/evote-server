package com.evote.server.repository;

import com.evote.server.model.TempOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TempOtpRepository extends JpaRepository<TempOtp, Long> {
    Optional<TempOtp> findFirstByIdentityCardNumberOrderByCreatedAtDesc(String identityCardNumber);
    void deleteByIdentityCardNumber(String identityCardNumber);
}
