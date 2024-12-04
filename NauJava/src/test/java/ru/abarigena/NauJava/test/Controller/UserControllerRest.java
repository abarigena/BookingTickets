package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.UserController.UserControllerRest;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserControllerRest.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testCreateUser_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setAge(25);
        user.setPhoneNumber("1234567890");

        doNothing().when(userService).addUser(user);

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"firstName\":\"Test\",\"lastName\":\"User\",\"age\":25,\"phoneNumber\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testFindUser() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        when(userService.findById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/users/find/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testFindAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setAge(30);
        updatedUser.setPhoneNumber("0987654321");

        User user = new User();
        user.setId(userId);
        user.setUsername("existinguser");

        when(userService.findById(userId)).thenReturn(user);
        when(userService.updateUser(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyString()
        )).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/update/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated\",\"lastName\":\"User\",\"age\":30,\"phoneNumber\":\"0987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }
}