package org.example.poemcraft.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Entity.Poem;
import org.example.poemcraft.Security.JwtUtil;
import org.example.poemcraft.Service.PoemService;
import org.example.poemcraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/poems")
public class PoemController {
    @Autowired
    private final PoemService poemService;
    @Autowired
    private final UserService userService; // 添加 UserService 注入
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public PoemController(PoemService poemService, UserService userService) {  // 构造器注入
        this.poemService = poemService;
        this.userService = userService;  // 初始化 UserService
    }

    // 添加内容
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Poem> addPoem(@RequestBody Map<String, Object> poemData) {
        // Ensure userID is retrieved as an Integer
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
    
        // 查找用户
        Optional<User> userOptional = userService.getUserById(userId);
    
        // 检查用户是否存在
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 返回 404
        }
    
        User user = userOptional.get(); // 获取 User 对象
    
        // 创建 Poem 对象
        Poem poem = new Poem();
        poem.setTitle((String) poemData.get("title"));
        poem.setContent((String) poemData.get("content"));
        poem.setGenre((String) poemData.get("genre"));
        poem.setTheme((String) poemData.get("theme"));
        poem.setUser(user); // 设置关联的 User 对象
    
        // 保存诗歌
        Poem savedPoem = poemService.addPoem(poem);
        
        return ResponseEntity.ok(savedPoem);
    }
    
    



    // 查看所有内容
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/getPoem")
    public ResponseEntity<?> getUserPoems(){
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

        // Find user by ID
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"); // Return 404 if user is not found
        }

        // Retrieve all poems by user ID
        List<Poem> userPoems = poemService.getPoemsByUserId(userId);

        // Extract content, genre, and theme from each poem
        List<Map<String, String>> poemData = userPoems.stream().map(poem -> Map.of(
                "content", poem.getContent(),
                "genre", poem.getGenre(),
                "theme", poem.getTheme()
        )).collect(Collectors.toList());
        System.out.println("Poem data to be returned: " + poemData);
        return ResponseEntity.ok(poemData);
    }

    // 删除内容
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoem(@PathVariable Integer id) {
        poemService.deletePoem(id);
        return ResponseEntity.noContent().build();
    }
}