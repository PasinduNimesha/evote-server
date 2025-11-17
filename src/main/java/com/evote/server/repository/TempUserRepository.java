package com.evote.server.repository;

import com.evote.server.model.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TempUserRepository extends JpaRepository<TempUser, String> {
    Optional<TempUser> findByIdentityCardNumber(String identityCardNumber);
}