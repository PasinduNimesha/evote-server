package com.evote.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_password")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPassword {

    @Id
    @Column(name = "identity_card_number", length = 20)
    private String identityCardNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String role = "USER";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "identity_card_number")
    private User user;
}
