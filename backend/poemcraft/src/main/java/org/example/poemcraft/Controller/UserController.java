package org.example.poemcraft.Controller;

import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Service.UserService;
import org.example.poemcraft.Service.EmailService;
import org.example.poemcraft.Security.JwtUtil;
import org.example.poemcraft.Security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordUtil passwordUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, EmailService emailService, PasswordUtil passwordUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.passwordUtil = passwordUtil;
    }

    // 发送邮件验证码
    @PostMapping("/send-code")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();
        String email = requestData.get("email");

        // 验证输入的 email 是否为空
        if (email == null || email.isEmpty()) {
            response.put("success", false);
            response.put("message", "Invalid email address provided.");
            return ResponseEntity.ok(response);
        }

        // 验证邮箱是否存在于数据库中
        boolean emailExists = userService.emailExists(email);
        if (emailExists) {
            response.put("success", false);
            response.put("message", "Email address is already registered.");
            return ResponseEntity.ok(response);
        }

        // 发送验证码
        emailService.sendVerificationCode(email);
        response.put("success", true);
        response.put("message", "Verification code sent to email: " + email);
        return ResponseEntity.ok(response);
    }



    // 用户注册
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();
        String email = requestData.get("email");
        String verificationCode = requestData.get("verificationCode");

        // 验证验证码
        if (!emailService.verifyCode(email, verificationCode)) {
            response.put("success", false);
            response.put("message", "The verification code is incorrect or expired.");
            return ResponseEntity.ok(response);
        }

        // 验证通过，继续注册
        User user = new User();
        user.setUsername(requestData.get("username"));
        user.setEmail(email);

        // 对密码进行加盐处理
        String salt = passwordUtil.generateSalt();
        String saltedPassword = passwordUtil.hashPassword(requestData.get("password"), salt);
        user.setPassword(saltedPassword);
        user.setSalt(salt);

        user.setRole(requestData.get("role"));
        user.setAvatar(requestData.get("avatar"));
        user.setPreference(requestData.get("preference"));

        try {
            User createdUser = userService.createUser(user);
            String token = jwtUtil.generateToken(createdUser.getId());

            response.put("success", true);
            response.put("message", "Registration successful!");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred during registration. Please try again.");
            return ResponseEntity.ok(response);
        }
    }



    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String salt = user.getSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
            if (user.getPassword().equals(hashedPassword)) {
                String token = jwtUtil.generateToken(user.getId());
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(401).body("密码错误");
            }
        } else {
            return ResponseEntity.status(404).body("用户不存在");
        }
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
