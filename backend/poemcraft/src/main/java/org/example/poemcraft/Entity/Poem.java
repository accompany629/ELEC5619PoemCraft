package org.example.poemcraft.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "poem")
public class Poem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PoemID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID")
    private User user;  // Changed to 'user' for clarity

    @Column(name = "Title")
    private String title;

    @Lob
    @Column(name = "Content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "Genre", length = 100)
    private String genre;

    @Column(name = "Theme", length = 100)
    private String theme;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {  // Updated getter method
        return user;
    }

    public void setUser(User user) {  // Updated setter method
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
