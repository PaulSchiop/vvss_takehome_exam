package com.vvss.exam.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequestDTO {
    @NotNull(message = "Player ID is required")
    private Long playerId;

    @NotNull(message = "Team ID is required")
    private Long teamId;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be strictly positive")
    private Integer salary;
}
