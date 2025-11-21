package com.evote.server.service;

import com.evote.dto.ApiResponse;
import com.evote.dto.OtpVerifyRequest;
import com.evote.dto.VoteStoreRequest;
import com.evote.model.Candidate;
import com.evote.model.TempOtp;
import com.evote.model.User;
import com.evote.model.UserVoteCheck;
import com.evote.model.Vote;
import com.evote.repository.CandidateRepository;
import com.evote.repository.TempOtpRepository;
import com.evote.repository.UserRepository;
import com.evote.repository.UserVoteCheckRepository;
import com.evote.repository.VoteRepository;
import com.evote.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {

    private final TempOtpRepository tempOtpRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;
    private final UserVoteCheckRepository userVoteCheckRepository;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
    public ApiResponse requestOtpToVote() {
        String identityCardNumber = getCurrentUsername();

        User user = userRepository.findById(identityCardNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = otpService.generateOtp();

        TempOtp tempOtp = TempOtp.builder()
                .identityCardNumber(identityCardNumber)
                .otpHash(passwordEncoder.encode(otp))
                .otpExpiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        tempOtpRepository.save(tempOtp);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return ApiResponse.success("OTP sent to your email. Please verify to continue.",
                Map.of("send", true));
    }

    @Transactional
    public List<Candidate> verifyOtpAndSendCandidateList(OtpVerifyRequest request) {
        String identityCardNumber = getCurrentUsername();

        TempOtp tempOtp = tempOtpRepository
                .findFirstByIdentityCardNumberOrderByCreatedAtDesc(identityCardNumber)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!passwordEncoder.matches(request.getOtp(), tempOtp.getOtpHash())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (LocalDateTime.now().isAfter(tempOtp.getOtpExpiresAt())) {
            tempOtpRepository.delete(tempOtp);
            throw new RuntimeException("OTP has expired");
        }

        List<Candidate> candidates = candidateRepository.findAll();
        tempOtpRepository.delete(tempOtp);

        return candidates;
    }

    @Transactional
    public Map<String, Object> storeVerifiedVote(VoteStoreRequest request) {
        String identityCardNumber = getCurrentUsername();

        // Check if user has already voted
        userVoteCheckRepository.findByIdentityCardNumber(identityCardNumber)
                .ifPresent(userVoteCheck -> {
                    if (userVoteCheck.getVoteStatus()) {
                        throw new RuntimeException("User has already voted");
                    }
                });

        // Store the vote
        Vote vote = Vote.builder()
                .candidateId(request.getCandidateId())
                .build();

        voteRepository.save(vote);

        // Mark user as voted
        UserVoteCheck userVoteCheck = UserVoteCheck.builder()
                .identityCardNumber(identityCardNumber)
                .voteStatus(true)
                .build();

        userVoteCheckRepository.save(userVoteCheck);

        // Generate verification token
        String voteVerifyToken = jwtUtil.generateToken(identityCardNumber, "USER");

        // Send verification email
        User user = userRepository.findById(identityCardNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        emailService.sendVerificationCodeEmail(user.getEmail(), voteVerifyToken);

        return Map.of(
                "message", "Vote stored successfully!",
                "voteVerifyToken", voteVerifyToken
        );
    }

    public ApiResponse verifyVoteSubmitSuccess(String voteVerifyToken) {
        try {
            String identityCardNumber = jwtUtil.extractUsername(voteVerifyToken);

            UserVoteCheck userVoteCheck = userVoteCheckRepository
                    .findByIdentityCardNumber(identityCardNumber)
                    .orElseThrow(() -> new RuntimeException("Invalid token submitted"));

            if (!userVoteCheck.getVoteStatus()) {
                return ApiResponse.error("User vote not successfully stored");
            }

            return ApiResponse.success("Vote successfully stored");
        } catch (Exception e) {
            log.error("Error verifying vote submission", e);
            return ApiResponse.error("Invalid or expired verification token");
        }
    }
}
