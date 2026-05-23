package com.vvss.exam.integration;

import com.vvss.exam.entity.Contract;
import com.vvss.exam.entity.Player;
import com.vvss.exam.entity.Team;
import com.vvss.exam.repository.ContractRepository;
import com.vvss.exam.repository.PlayerRepository;
import com.vvss.exam.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class ContractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ContractRepository contractRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testContractFlow() throws Exception {
        // Step A: Create and save a Player and a Team
        Player player = new Player();
        player.setName("John Doe");
        player.setPosition("Forward");
        player = playerRepository.save(player);

        Team team = new Team();
        team.setCity("Cluj-Napoca");
        team.setName("CFR Cluj");
        team = teamRepository.save(team);

        // Step B: Perform a POST request to /contracts/sign
        mockMvc.perform(post("/contracts/sign")
                        .param("playerId", player.getId().toString())
                        .param("teamId", team.getId().toString())
                        .param("salary", "50000"))
                // Step C: Verify the response is a redirect to the report page
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contracts/report"));

        // Step D: Perform a GET request to /contracts/report?teamId={id}
        mockMvc.perform(get("/contracts/report")
                        .param("teamId", team.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("report"))
                .andExpect(content().string(containsString("John Doe")))
                .andExpect(content().string(containsString("50000")));

        // Step E: Verify directly via ContractRepository
        List<Contract> contracts = contractRepository.findByTeamId(team.getId());
        assertFalse(contracts.isEmpty(), "A contract record should exist in the repository");
        assertEquals(1, contracts.size());
        assertEquals(player.getId(), contracts.get(0).getPlayer().getId());
        assertEquals(50000, contracts.get(0).getSalary());
    }
}
