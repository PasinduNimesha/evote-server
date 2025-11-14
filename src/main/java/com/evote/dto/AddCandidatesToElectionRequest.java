package com.evote.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCandidatesToElectionRequest {

    @NotNull(message = "Election ID is required")
    private Long electionId;

    @NotEmpty(message = "At least one candidate ID is required")
    private List<Long> candidateIds;
}
