package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;

    @Test
    public void testGetUserById_success() throws Exception{
        User mockEmployee = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();

        when(userService.getUserById(1)).thenReturn(mockEmployee);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/getUser/{id}", "1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        User user = new Gson().fromJson(result.getResponse().getContentAsString(), User.class);
        assertEquals(mockEmployee.toString(), user.toString());
    }

}
