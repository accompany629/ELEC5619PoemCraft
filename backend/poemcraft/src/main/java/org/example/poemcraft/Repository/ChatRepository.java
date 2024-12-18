package org.example.poemcraft.Repository;

import org.example.poemcraft.Entity.Chat;
import org.example.poemcraft.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findByUserID(User user);
    // 在这里你可以定义一些自定义查询方法

    Optional<Chat> findByUserIDAndGenreAndTopic(User userID, String genre, String topic);
}
