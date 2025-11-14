package com.evote.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "result")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @Column(name = "candidate_id")
    private Long candidateId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "total_vote", nullable = false)
    @Builder.Default
    private Integer totalVote = 0;

    @OneToOne
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private Candidate candidate;
}
