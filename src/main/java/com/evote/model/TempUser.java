package com.evote.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "temp_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempUser {

    @Id
    @Column(name = "identity_card_number", length = 20)
    private String identityCardNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @Column(name = "residence_id", nullable = false, length = 100)
    private String residenceId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "date_of_birth", nullable = false, length = 100)
    private String dateOfBirth;

    @Column(name = "otp_hash", nullable = false)
    private String otpHash;

    @Column(name = "otp_expires_at", nullable = false)
    private LocalDateTime otpExpiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}