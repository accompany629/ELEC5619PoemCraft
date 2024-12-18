package org.example.poemcraft.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID")
    private User userID;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "User_Experience")
    private Integer userExperience;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Functionality")
    private Integer functionality;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Content_Quality")
    private Integer contentQuality;

    @Lob
    @Column(name = "Feedback")
    private String feedback;

    @Column(name = "CreatedAt", nullable = false)
    private Instant createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public Integer getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(Integer userExperience) {
        this.userExperience = userExperience;
    }

    public Integer getFunctionality() {
        return functionality;
    }

    public void setFunctionality(Integer functionality) {
        this.functionality = functionality;
    }

    public Integer getContentQuality() {
        return contentQuality;
    }

    public void setContentQuality(Integer contentQuality) {
        this.contentQuality = contentQuality;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}