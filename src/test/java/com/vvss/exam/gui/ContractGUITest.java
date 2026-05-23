package com.vvss.exam.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class ContractGUITest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
}
