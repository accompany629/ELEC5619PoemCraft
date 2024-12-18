package org.example.poemcraft.Controller;

import org.example.poemcraft.Entity.Content;
import org.example.poemcraft.Service.OpenAIService;
import org.example.poemcraft.Service.ContentService;
import org.example.poemcraft.Entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.time.Instant;
import java.util.Map;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Repository.UserRepository;
import org.example.poemcraft.Security.JwtUtil;
import java.util.Optional;


@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final OpenAIService openAIService;
    private final ContentService contentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    public ContentController(OpenAIService openAIService, ContentService contentService) {
        this.openAIService = openAIService;
        this.contentService = contentService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Content> generateContent(@RequestBody Map<String, String> request) {
        String userInput = request.get("input");
        Integer chatId = Integer.parseInt(request.get("chatId"));

        // 保存用户输入内容
        Content userContent = new Content();
        userContent.setRole("user");  // 用户的角色为 "user"
        userContent.setText(userInput);
        Chat chat = new Chat();
        chat.setId(chatId);
        userContent.setChat(chat);
        contentService.saveContent(userContent);

        // 调用 OpenAI API 并保存 AI 回复内容
        String responseText = openAIService.generateResponse(userInput);
        Content aiContent = new Content();
        aiContent.setRole("Bot");  // AI 的角色为 "Bot"
        aiContent.setText(responseText);
        aiContent.setChat(chat);
        contentService.saveContent(aiContent);

        return ResponseEntity.ok(aiContent);
    }




    @GetMapping("/getContent")
    public List<ContentDTO> getContentByChatId(@RequestParam Integer ChatID) {
        org.example.poemcraft.Entity.Chat chat = new org.example.poemcraft.Entity.Chat();
        chat.setId(ChatID);
        List<Content> contentList = contentService.getContentsByChat(chat);
        return contentList.stream()
                .map(content -> new ContentDTO(content.getRole(), content.getText()))
                .collect(Collectors.toList());
    }

    // DTO 类
    public static class ContentDTO {
        private String role;
        private String text;

        public ContentDTO(String role, String text) {
            this.role = role;
            this.text = text;
        }

        public String getRole() {
            return role;
        }

        public String getText() {
            return text;
        }
    }
}
