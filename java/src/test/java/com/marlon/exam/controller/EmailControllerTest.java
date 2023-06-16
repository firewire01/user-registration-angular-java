package com.marlon.exam.controller;

import com.marlon.exam.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser
class EmailControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmailService service;


    @Test
    @WithMockUser("mine")
    void getAllEmails() throws Exception {

        when(service.getAllSent()).thenReturn(Arrays.asList());

        this.mockMvc.perform(get("/api/emails"))
                .andDo(print()).andExpect(status().isOk());
    }
}