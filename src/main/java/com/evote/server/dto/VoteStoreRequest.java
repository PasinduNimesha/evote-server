package com.evote.server.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteStoreRequest {

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotNull(message = "Signature is required")
    private String signature;
}