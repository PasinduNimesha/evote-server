package com.evote.server.service;

import com.evote.server.dto.AddCandidatesToElectionRequest;
import com.evote.server.dto.ApiResponse;
import com.evote.server.dto.ElectionRequest;
import com.evote.server.dto.RemoveCandidateFromElectionRequest;
import com.evote.server.model.Candidate;
import com.evote.server.model.Election;
import com.evote.server.repository.CandidateRepository;
import com.evote.server.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    public Election getElectionById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Election not found with id: " + id));
    }

    @Transactional
    public Election createElection(ElectionRequest request) {
        Election election = Election.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        return electionRepository.save(election);
    }

    @Transactional
    public Election updateElection(Long id, ElectionRequest request) {
        Election election = getElectionById(id);

        election.setName(request.getName());
        election.setStartDate(request.getStartDate());
        election.setEndDate(request.getEndDate());

        return electionRepository.save(election);
    }

    @Transactional
    public void deleteElection(Long id) {
        Election election = getElectionById(id);
        electionRepository.delete(election);
    }

    public Election getElectionWithCandidates(Long id) {
        return getElectionById(id);
    }

    @Transactional
    public ApiResponse addCandidatesToElection(AddCandidatesToElectionRequest request) {
        Election election = getElectionById(request.getElectionId());

        List<Candidate> candidates = candidateRepository.findAllById(request.getCandidateIds());

        if (candidates.size() != request.getCandidateIds().size()) {
            return ApiResponse.error("Some candidate IDs were not found");
        }

        election.getCandidates().addAll(candidates);
        electionRepository.save(election);

        return ApiResponse.success("Candidates added to election successfully");
    }

    @Transactional
    public void removeCandidateFromElection(RemoveCandidateFromElectionRequest request) {
        Election election = getElectionById(request.getElectionId());
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        election.getCandidates().remove(candidate);
        electionRepository.save(election);
    }
}
