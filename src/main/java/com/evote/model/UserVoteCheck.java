package com.evote.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_vote_check")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVoteCheck {

    @Id
    @Column(name = "identity_card_number", length = 20)
    private String identityCardNumber;

    @Column(name = "vote_status", nullable = false)
    private Boolean voteStatus;

    @OneToOne
    @JoinColumn(name = "identity_card_number", insertable = false, updatable = false)
    private User user;
}