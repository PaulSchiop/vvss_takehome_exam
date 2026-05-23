# Task 2: Black-Box Testing Design

## Method to Test: `ContractService.signFreeAgent(ContractRequestDTO request)`

The goal is to test the business logic of signing a free agent to a team.

### 1. Equivalence Class Partitioning (ECP)

| Input Parameter | Valid Classes | Invalid Classes |
| :--- | :--- | :--- |
| `playerId` | **EC1**: Existing player ID (Long) | **EC2**: Non-existing player ID (Long)<br>**EC3**: null |
| `teamId` | **EC4**: Existing team ID (Long) | **EC5**: Non-existing team ID (Long)<br>**EC6**: null |
| `salary` | **EC7**: Positive integer (> 0) | **EC8**: Zero (0)<br>**EC9**: Negative integer (< 0)<br>**EC10**: null |

### 2. Boundary Value Analysis (BVA) for `salary`

| Boundary Value | Expected Result | Description |
| :--- | :--- | :--- |
| `salary = 0` | Invalid | Minimum invalid value (boundary) |
| `salary = 1` | Valid | Minimum valid value |
| `salary = Integer.MAX_VALUE` | Valid | Maximum valid value |
| `salary = -1` | Invalid | Negative value |

### 3. Test Cases Selection

| Test Case ID | Description | `playerId` | `teamId` | `salary` | Expected Result |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC1** | Valid Case (Success) | 1 (Existing) | 1 (Existing) | 50000 | `ContractDTO` returned |
| **TC2** | Player Not Found | 99 (Non-existing) | 1 (Existing) | 50000 | `ResourceNotFoundException` |
| **TC3** | Team Not Found | 1 (Existing) | 99 (Non-existing) | 50000 | `ResourceNotFoundException` |
| **TC4** | Salary Min Valid | 1 (Existing) | 1 (Existing) | 1 | `ContractDTO` returned |
| **TC5** | Salary Zero | 1 (Existing) | 1 (Existing) | 0 | Exception or Validation Error* |
| **TC6** | Salary Negative | 1 (Existing) | 1 (Existing) | -1 | Exception or Validation Error* |

*\*Note: In a pure Unit Test of the service without `@Valid` enforcement, these might pass if the service doesn't explicitly check. However, for BBT we expect these to be invalid according to requirements.*
