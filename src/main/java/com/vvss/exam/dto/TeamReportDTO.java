package com.vvss.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamReportDTO {
    private String teamName;
    private List<ContractDTO> contracts;
    private Integer totalPayroll;
}
