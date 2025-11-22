package com.evote.server.service;

import com.evote.server.dto.ApiResponse;
import com.evote.server.model.Candidate;
import com.evote.server.model.Result;
import com.evote.server.model.UserVoteCheck;
import com.evote.server.repository.CandidateRepository;
import com.evote.server.repository.ResultRepository;
import com.evote.server.repository.UserVoteCheckRepository;
import com.evote.server.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteControlService {

    private final VoteRepository voteRepository;
    private final ResultRepository resultRepository;
    private final UserVoteCheckRepository userVoteCheckRepository;
    private final CandidateRepository candidateRepository;

    @Transactional
    public ApiResponse startVote() {
        // Clear previous voting data
        voteRepository.deleteAll();
        resultRepository.deleteAll();
        userVoteCheckRepository.deleteAll();

        // Initialize results for all candidates
        List<Candidate> candidates = candidateRepository.findAll();

        for (Candidate candidate : candidates) {
            Result result = Result.builder()
                    .candidateId(candidate.getCandidateId())
                    .name(candidate.getName())
                    .totalVote(0)
                    .build();

            resultRepository.save(result);
        }

        return ApiResponse.success("Vote started successfully");
    }

    @Transactional
    public ApiResponse endVote() {
        List<Candidate> candidates = candidateRepository.findAll();

        for (Candidate candidate : candidates) {
            long totalVotes = voteRepository.countByCandidateId(candidate.getCandidateId());

            Result result = resultRepository.findById(candidate.getCandidateId())
                    .orElse(Result.builder()
                            .candidateId(candidate.getCandidateId())
                            .name(candidate.getName())
                            .totalVote(0)
                            .build());

            result.setTotalVote((int) totalVotes);
            resultRepository.save(result);
        }

        return ApiResponse.success("Vote ended successfully");
    }

    public List<Result> getResults() {
        return resultRepository.findAll();
    }

    public List<UserVoteCheck> getUniversalVerifiability() {
        return userVoteCheckRepository.findAll();
    }
}
