package org.example.poemcraft.Controller;

import org.example.poemcraft.Repository.UserRepository;
import org.example.poemcraft.Security.JwtUtil;
import org.example.poemcraft.Service.ContentService;
import org.example.poemcraft.Service.OpenAIService;
import org.example.poemcraft.Entity.Chat;
import org.example.poemcraft.Entity.Content;
import org.example.poemcraft.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(ContentController.class)
public class ContentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenAIService openAIService;

    @MockBean
    private ContentService contentService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGenerateContent_Success() throws Exception {
        mockMvc.perform(post("/api/content/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"input\":\"Hello, how are you?\",\"chatId\":\"1\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetContentByChatId_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content/getContent")
                        .param("ChatID", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }
}

