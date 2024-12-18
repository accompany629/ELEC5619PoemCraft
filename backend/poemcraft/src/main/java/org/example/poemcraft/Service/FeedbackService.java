package org.example.poemcraft.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.poemcraft.Entity.Feedback;
import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Repository.FeedbackRepository;
import org.example.poemcraft.Repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    public Feedback createFeedback(Feedback feedbackData, Integer userId) {
        // Find the user by ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();

        // Create and populate the Feedback object
        Feedback feedback = new Feedback();
        feedback.setUserID(user);
        feedback.setUserExperience(feedbackData.getUserExperience());
        feedback.setFunctionality(feedbackData.getFunctionality());
        feedback.setContentQuality(feedbackData.getContentQuality());
        feedback.setFeedback(feedbackData.getFeedback());
        feedback.setCreatedAt(Instant.now());

        // Save feedback to the repository
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}
