package org.example.poemcraft.Controller;

import org.example.poemcraft.Entity.Chat;
import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Repository.ChatRepository;
import org.example.poemcraft.Repository.ContentRepository;
import org.example.poemcraft.Repository.UserRepository;
import org.example.poemcraft.Security.JwtUtil;
import org.example.poemcraft.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.Optional;

@WebMvcTest(ChatController.class)
public class ChatControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRepository chatRepository;

    @MockBean
    private UserService userService;


    @MockBean
    private ContentRepository contentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateChat_Success() throws Exception {
        // Mock JWT validation and User lookup
        when(jwtUtil.validateTokenAndGetUserId("valid-token")).thenReturn(1);
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Mock Chat save
        Chat chat = new Chat();
        chat.setId(1);
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        // Perform request
        mockMvc.perform(post("/api/chats/create")
                        .with(csrf())  // 添加此行来模拟 CSRF 令牌
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid-token")
                        .content("{\"genre\":\"Poetry\",\"topic\":\"Love\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.chatId").value(1));
    }


    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetChatsByUser_Success() throws Exception {
        // Mock JWT validation and User lookup
        when(jwtUtil.validateTokenAndGetUserId("valid-token")).thenReturn(1);
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Mock Chat retrieval
        Chat chat = new Chat();
        chat.setId(1);
        chat.setGenre("Poetry");
        chat.setTopic("Love");
        when(chatRepository.findByUserID(user)).thenReturn(List.of(chat));

        // Perform request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chats/user")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].chatId").value(1))
                .andExpect(jsonPath("$[0].description").value("Poetry : Love"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetChatsByUser_Unauthorized() throws Exception {
        // Perform request without token
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chats/user"))
                .andExpect(status().isUnauthorized());
    }
}
