package com.evote.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteVerifyRequest {
    @NotBlank(message = "Vote verify token is required")
    private String voteVerifyToken;
}
