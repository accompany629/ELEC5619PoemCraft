package org.example.poemcraft.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.poemcraft.Entity.Feedback;
import org.example.poemcraft.Security.JwtUtil;
import org.example.poemcraft.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(@RequestBody Feedback feedbackData) {

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");

        // Validate the JWT token and extract the user ID
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        token = token.substring(7);
        Integer userId = jwtUtil.validateTokenAndGetUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Create and save feedback
        Feedback feedback = feedbackService.createFeedback(feedbackData, userId);

        return ResponseEntity.ok(feedback);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbackList);
    }
}
