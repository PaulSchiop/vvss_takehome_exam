# Task 2: White-Box Testing Design

## Method under test: `ContractService.getTeamPayrollReport(Long teamId)`

### Source Code Analysis

```java
public TeamReportDTO getTeamPayrollReport(Long teamId) {
    TeamDTO team = teamService.findById(teamId); // Decision Point 1: Throws Exception if not found
    List<Contract> contracts = contractRepository.findByTeamId(teamId);
    List<ContractDTO> contractDTOs = contracts.stream()
            .map(this::mapToDTO) // Implicit Loop: Iterates over contracts
            .collect(Collectors.toList());
    Integer totalPayroll = contracts.stream()
            .map(Contract::getSalary) // Implicit Loop: Iterates over contracts
            .reduce(0, Integer::sum);
    
    return new TeamReportDTO(team.getName(), contractDTOs, totalPayroll);
}
```

### Control Flow Graph (CFG) Elements

1.  **Node A**: Start `getTeamPayrollReport(teamId)`
2.  **Node B**: `teamService.findById(teamId)`
3.  **Decision Point 1**: Does `teamService.findById` throw `ResourceNotFoundException`?
    *   **Yes (Branch 1-F)**: Propagate Exception (End)
    *   **No (Branch 1-T)**: Continue to Node C
4.  **Node C**: `contractRepository.findByTeamId(teamId)`
5.  **Node D**: Map contracts to DTOs and calculate `totalPayroll`.
    *   Note: Streams handle empty collections gracefully.
6.  **Node E**: Return `TeamReportDTO` (End)

### Identified Paths for 100% Coverage

#### Path 1: Team Not Found (Exception Path)
*   **Input**: `teamId = 999L` (non-existent)
*   **Mocks**: `teamService.findById(999L)` throws `ResourceNotFoundException`.
*   **Execution Flow**: A -> B -> Branch 1-F -> Exception
*   **Coverage**: Decision Point 1 (False), Statement Coverage for Exception handling.

#### Path 2: Team Exists, Zero Contracts
*   **Input**: `teamId = 1L`
*   **Mocks**:
    *   `teamService.findById(1L)` returns `TeamDTO("Lakers")`.
    *   `contractRepository.findByTeamId(1L)` returns `Collections.emptyList()`.
*   **Execution Flow**: A -> B -> Branch 1-T -> C -> D -> E
*   **Expected Outcome**: `TeamReportDTO` with `teamName="Lakers"`, `contracts=[]`, `totalPayroll=0`.
*   **Coverage**: Decision Point 1 (True), Statements in Node C, D, E.

#### Path 3: Team Exists, Multiple Contracts
*   **Input**: `teamId = 1L`
*   **Mocks**:
    *   `teamService.findById(1L)` returns `TeamDTO("Lakers")`.
    *   `contractRepository.findByTeamId(1L)` returns list with 2 contracts (Salaries: 1000, 2000).
*   **Execution Flow**: A -> B -> Branch 1-T -> C -> D -> E
*   **Expected Outcome**: `TeamReportDTO` with `teamName="Lakers"`, `contracts` size 2, `totalPayroll=3000`.
*   **Coverage**: Loop coverage (multiple iterations), all remaining statements.
