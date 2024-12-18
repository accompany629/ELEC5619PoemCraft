package org.example.poemcraft.Controller;

import org.example.poemcraft.Entity.Chat;
import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Security.JwtUtil;
import org.example.poemcraft.Repository.ChatRepository;
import org.example.poemcraft.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.example.poemcraft.Entity.Content;
import org.example.poemcraft.Repository.ContentRepository;



import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 创建一个新的 Chat
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createChat(@RequestBody Chat chatRequest) {
        // 从请求头中获取 JWT token
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        token = token.substring(7); // 移除 "Bearer " 前缀

        // 验证 token 并提取用户 ID
        Integer userId = jwtUtil.validateTokenAndGetUserId(token);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        chatRequest.setUserID(user);
        Chat savedChat = chatRepository.save(chatRequest);

        // 返回 chatId
        Map<String, Object> response = new HashMap<>();
        response.put("chatId", savedChat.getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<List<ChatDTO>> getChatsByUser() {
        // 从请求头中获取 JWT token
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        token = token.substring(7); // 移除 "Bearer " 前缀

        // 验证 token 并提取用户 ID
        Integer userId = jwtUtil.validateTokenAndGetUserId(token);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 查找用户的所有聊天记录
        List<Chat> chats = chatRepository.findByUserID(userOptional.get());

        // 组合每个聊天记录的 ChatID、Genre 和 Topic，隐藏用户信息
        List<ChatDTO> combinedChats = chats.stream()
                .map(chat -> {
                    chat.setUserID(null); // 隐藏用户信息
                    String genre = chat.getGenre() != null ? chat.getGenre() : "";
                    String topic = chat.getTopic() != null ? chat.getTopic() : "";
                    return new ChatDTO(chat.getId(), genre + " : " + topic);
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(combinedChats, HttpStatus.OK);
    }

    // DTO 类
    public static class ChatDTO {
        private Integer chatId;
        private String description;

        public ChatDTO(Integer chatId, String description) {
            this.chatId = chatId;
            this.description = description;
        }

        public Integer getChatId() {
            return chatId;
        }

        public String getDescription() {
            return description;
        }
    }
}
