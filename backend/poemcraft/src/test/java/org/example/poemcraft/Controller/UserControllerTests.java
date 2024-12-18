package org.example.poemcraft.Controller;

import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Security.JwtUtil;
import org.example.poemcraft.Security.PasswordUtil;
import org.example.poemcraft.Service.EmailService;
import org.example.poemcraft.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private EmailService emailService;

    @MockBean
    private PasswordUtil passwordUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testSendVerificationCode_Success() throws Exception {
        mockMvc.perform(post("/api/users/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Verification code sent to email: test@example.com"));
    }



    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateUser_Success() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"verificationCode\":\"123456\",\"username\":\"testuser\",\"password\":\"password\",\"role\":\"USER\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("The verification code is incorrect or expired."));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testLogin_Success() throws Exception {
        User user = new User();
        user.setId(1);
        user.setSalt("random_salt_value");
        user.setPassword("Ck3XN6H3qx8Ljzb37XWVn+utPemWtdWod5VHpwg//xY=");

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        // Skipping PasswordUtil.hashPassword mocking as it's not a mockable static method.
        when(jwtUtil.generateToken(1)).thenReturn("valid-token");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("valid-token"));
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteUser_Success() throws Exception {
        int userId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
