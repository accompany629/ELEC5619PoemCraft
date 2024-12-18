package org.example.poemcraft.Service;

import org.example.poemcraft.Entity.Chat;
import org.example.poemcraft.Entity.User;
import org.example.poemcraft.Repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    // 构造函数注入
    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    // 创建或更新Chat
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    // 获取所有Chat
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    // 通过ID获取Chat
    public Optional<Chat> getChatById(Integer id) {
        return chatRepository.findById(id);
    }

    // 更新Chat
    public Optional<Chat> updateChat(Integer id, Chat chatDetails) {
        return chatRepository.findById(id).map(chat -> {
            chat.setTopic(chatDetails.getTopic());
            chat.setGenre(chatDetails.getGenre());
            chat.setTheme(chatDetails.getTheme());
            chat.setStarStatus(chatDetails.getStarStatus());
            return chatRepository.save(chat);
        });
    }

    // 删除Chat
    public void deleteChat(Integer id) {
        chatRepository.deleteById(id);
    }








}
