package com.evote.server.repository;

import com.evote.server.model.UserVoteCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserVoteCheckRepository extends JpaRepository<UserVoteCheck, String> {
    Optional<UserVoteCheck> findByIdentityCardNumber(String identityCardNumber);
}
