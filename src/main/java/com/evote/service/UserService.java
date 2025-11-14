package com.evote.service;

import com.evote.server.dto.*;
import com.evote.server.model.TempUser;
import com.evote.server.model.User;
import com.evote.server.model.UserPassword;
import com.evote.server.repository.TempUserRepository;
import com.evote.server.repository.UserPasswordRepository;
import com.evote.server.repository.UserRepository;
import com.evote.server.security.AuthorityKeyManager;
import com.evote.server.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserPasswordRepository userPasswordRepository;
    private final TempUserRepository tempUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final AuthorityKeyManager authorityKeyManager;

    @Transactional
    public ApiResponse registerUser(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsById(request.getIdentityCardNumber())) {
            return ApiResponse.error("User already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("Email already registered");
        }

        // Generate OTP
        String otp = otpService.generateOtp();

        // Save temporary user with OTP
        TempUser tempUser = TempUser.builder()
                .identityCardNumber(request.getIdentityCardNumber())
                .name(request.getName())
                .mobileNumber(request.getMobileNumber())
                .residenceId(request.getResidenceId())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .otpHash(passwordEncoder.encode(otp))
                .otpExpiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        tempUserRepository.save(tempUser);

        // Send OTP email
        emailService.sendOtpEmail(request.getEmail(), otp);

        return ApiResponse.success("OTP sent to your email. Please verify to continue.");
    }

    @Transactional
    public ApiResponse verifyOtpAndRegisterPassword(VerifyOtpRequest request) {
        TempUser tempUser = tempUserRepository.findByIdentityCardNumber(request.getIdentityCardNumber())
                .orElseThrow(() -> new RuntimeException("User not found or OTP expired"));

        // Check if OTP has expired
        if (LocalDateTime.now().isAfter(tempUser.getOtpExpiresAt())) {
            tempUserRepository.delete(tempUser);
            return ApiResponse.error("OTP has expired. Please register again.");
        }

        // Verify OTP
        if (!passwordEncoder.matches(request.getOtp(), tempUser.getOtpHash())) {
            return ApiResponse.error("Invalid OTP");
        }

        // Create user
        User user = User.builder()
                .identityCardNumber(tempUser.getIdentityCardNumber())
                .name(tempUser.getName())
                .mobileNumber(tempUser.getMobileNumber())
                .residenceId(tempUser.getResidenceId())
                .email(tempUser.getEmail())
                .dateOfBirth(tempUser.getDateOfBirth())
                .build();

        userRepository.save(user);

        // Create user password
        UserPassword userPassword = UserPassword.builder()
                .identityCardNumber(user.getIdentityCardNumber())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .user(user)
                .build();

        userPasswordRepository.save(userPassword);

        // Remove temporary user
        tempUserRepository.delete(tempUser);

        return ApiResponse.success("User registered successfully!", user);
    }

    public LoginResponse loginUser(LoginRequest request) {
        UserPassword userPassword = userPasswordRepository.findById(request.getIdentityCardNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), userPassword.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(request.getIdentityCardNumber(), userPassword.getRole());

        // Send login confirmation email
        User user = userRepository.findById(request.getIdentityCardNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
        emailService.sendLoginConfirmation(user.getEmail());

        return LoginResponse.builder()
                .message("Login successful!")
                .token(token)
                .publicKeyPem(authorityKeyManager.getPublicKeyPem())
                .build();
    }
}
