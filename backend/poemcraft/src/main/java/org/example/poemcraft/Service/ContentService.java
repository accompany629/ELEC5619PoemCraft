package org.example.poemcraft.Service;

import org.example.poemcraft.Entity.Content;
import org.example.poemcraft.Repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    private final ContentRepository contentRepository;

    @Autowired
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public void saveContent(Content content) {
        contentRepository.save(content);
    }

    public Optional<Content> getContentById(Integer id) {
        return contentRepository.findById(id);
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    public void deleteContent(Integer id) {
        contentRepository.deleteById(id);
    }

    public List<Content> getContentsByChat(org.example.poemcraft.Entity.Chat chat) {
        return contentRepository.findByChat(chat);
    }


}
