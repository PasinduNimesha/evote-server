package com.evote.server.service;

import com.evote.server.dto.ApiResponse;
import com.evote.server.dto.CandidateRequest;
import com.evote.server.model.Candidate;
import com.evote.server.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
    }

    @Transactional
    public Candidate createCandidate(CandidateRequest request) {
        Candidate candidate = Candidate.builder()
                .name(request.getName())
                .age(request.getAge())
                .dateOfBirth(request.getDateOfBirth())
                .build();

        return candidateRepository.save(candidate);
    }

    @Transactional
    public Candidate updateCandidate(Long id, CandidateRequest request) {
        Candidate candidate = getCandidateById(id);

        candidate.setName(request.getName());
        candidate.setAge(request.getAge());
        candidate.setDateOfBirth(request.getDateOfBirth());

        return candidateRepository.save(candidate);
    }

    @Transactional
    public void deleteCandidate(Long id) {
        Candidate candidate = getCandidateById(id);
        candidateRepository.delete(candidate);
    }
}
