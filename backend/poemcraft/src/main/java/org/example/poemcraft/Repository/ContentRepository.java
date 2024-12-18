package org.example.poemcraft.Repository;

import org.example.poemcraft.Entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.poemcraft.Entity.Chat;

import java.util.List;
@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    List<Content> findByChat(Chat chat);
}
