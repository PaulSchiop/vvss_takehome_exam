package com.vvss.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    private Long id;
    private Integer salary;
    private PlayerDTO playerDTO;
    private TeamDTO teamDTO;
}
