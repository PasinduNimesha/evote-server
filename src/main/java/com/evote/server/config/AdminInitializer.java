package com.evote.server.config;


import com.evote.server.model.User;
import com.evote.server.model.UserPassword;
import com.evote.server.repository.UserPasswordRepository;
import com.evote.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserPasswordRepository userPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.identity-card-number}")
    private String adminId;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.role}")
    private String adminRole;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.mobile-number}")
    private String adminMobileNumber;

    @Value("${admin.residence-id}")
    private String adminResidenceId;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.date-of-birth}")
    private String adminDateOfBirth;

    @Override
    public void run(String... args) {
        try {
            // Check if admin already exists
            if (userRepository.existsById(adminId)) {
                log.info("Admin user already exists");
                return;
            }

            // Create admin user
            User adminUser = User.builder()
                    .identityCardNumber(adminId)
                    .name(adminName)
                    .mobileNumber(adminMobileNumber)
                    .residenceId(adminResidenceId)
                    .email(adminEmail)
                    .dateOfBirth(adminDateOfBirth)
                    .build();

            userRepository.save(adminUser);

            // Create admin password
            UserPassword adminUserPassword = UserPassword.builder()
                    .identityCardNumber(adminId)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .role(adminRole)
                    .user(adminUser)
                    .build();

            userPasswordRepository.save(adminUserPassword);

            log.info("Admin user created successfully with ID: {}", adminId);
        } catch (Exception e) {
            log.error("Error creating admin user: {}", e.getMessage());
        }
    }
}
