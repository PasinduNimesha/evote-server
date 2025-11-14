package com.evote.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vote")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @ManyToOne
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private Candidate candidate;
}
