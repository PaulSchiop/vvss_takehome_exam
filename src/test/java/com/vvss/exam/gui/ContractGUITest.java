package com.vvss.exam.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.vvss.exam.entity.Player;
import com.vvss.exam.entity.Team;
import com.vvss.exam.entity.Contract;
import com.vvss.exam.repository.PlayerRepository;
import com.vvss.exam.repository.TeamRepository;
import com.vvss.exam.repository.ContractRepository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class ContractGUITest {

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
    public void testRenderSignForm() throws Exception {
        mockMvc.perform(get("/contracts/sign"))
                .andExpect(status().isOk())
                .andExpect(view().name("contracts/sign"))
                .andExpect(content().string(containsString("<form")))
                .andExpect(content().string(containsString("id=\"playerId\"")))
                .andExpect(content().string(containsString("id=\"teamId\"")))
                .andExpect(content().string(containsString("id=\"salary\"")));
    }

    @Test
    public void testValidationFeedback() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/contracts/sign")
                        .param("playerId", "1")
                        .param("teamId", "1")
                        .param("salary", "-100"))
                .andExpect(status().isOk())
                .andExpect(view().name("contracts/sign"))
                .andExpect(content().string(containsString("Salary must be strictly positive")));
    }

    @Test
    public void testReportRendering() throws Exception {
        // Setup data
        Player player = new Player();
        player.setName("GUI Player");
        player.setPosition("Midfielder");
        player = playerRepository.save(player);

        Team team = new Team();
        team.setCity("London");
        team.setName("Chelsea");
        team = teamRepository.save(team);

        Contract contract = new Contract();
        contract.setPlayer(player);
        contract.setTeam(team);
        contract.setSalary(75000);
        contractRepository.save(contract);

        mockMvc.perform(get("/contracts/report")
                        .param("teamId", team.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("contracts/report"))
                .andExpect(content().string(containsString("Chelsea")))
                .andExpect(content().string(containsString("GUI Player")))
                .andExpect(content().string(containsString("75000")));
    }
}
