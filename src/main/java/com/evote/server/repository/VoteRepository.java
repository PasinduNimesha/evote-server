package com.evote.server.repository;

import com.evote.server.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    long countByCandidateId(Long candidateId);
}
