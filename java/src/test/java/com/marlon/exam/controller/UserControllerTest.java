package com.marlon.exam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlon.exam.dto.LoginRequest;
import com.marlon.exam.dto.UserRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService service;

    @Test
    void register() throws Exception {
        when(service.create(Mockito.any(UserRequest.class))).thenReturn(UserResponse.builder()
                .id(1L).build());

        ObjectMapper mapper = new ObjectMapper();

        UserRequest userRequest = UserRequest.builder()
                .firstName("name")
                .lastName("last")
                .email("sample@test.com")
                .age(1)
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        when(service.update(Mockito.anyLong(),Mockito.any(UserRequest.class))).thenReturn(UserResponse.builder()
                .id(1L).build());

        ObjectMapper mapper = new ObjectMapper();

        UserRequest userRequest = UserRequest.builder()
                .firstName("name")
                .lastName("last")
                .email("sample@test.com")
                .age(1)
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print()).andExpect(status().isAccepted());

    }

    @Test
    void updateUserCreds() throws Exception {

        when(service.updateUserCredentials(Mockito.anyLong(),Mockito.any(LoginRequest.class))).thenReturn(UserResponse.builder()
                .id(1L).build());

        ObjectMapper mapper = new ObjectMapper();

        LoginRequest loginRequest = LoginRequest.builder()
                .userName("last")
                .password("sample@test.com")
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/user/1/update-credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andDo(print()).andExpect(status().isAccepted());
    }

    @Test
    void get() throws Exception {
        when(service.get(Mockito.anyLong())).thenReturn(UserResponse.builder()
                .id(1L).build());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(Arrays.asList(UserResponse.builder()
                .id(1L).build()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void delete() throws Exception {
        doNothing().when(service).delete(Mockito.anyLong());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {

        doNothing().when(service).delete(Mockito.anyList());

        ObjectMapper mapper = new ObjectMapper();
        List<Long> ids = new ArrayList<>();
        ids.add(1l);
        ids.add(2l);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ids)))
                .andDo(print()).andExpect(status().isOk());
    }
}