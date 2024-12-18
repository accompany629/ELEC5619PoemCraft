package org.example.poemcraft.Service;

import org.example.poemcraft.Entity.Poem;
import org.example.poemcraft.Repository.PoemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoemService {

    private final PoemRepository poemRepository;

    @Autowired
    public PoemService(PoemRepository poemRepository) {
        this.poemRepository = poemRepository;
    }

    // 添加内容
    public Poem addPoem(Poem poem) {
        return poemRepository.save(poem);
    }

    // 查看所有内容
    public List<Poem> getAllPoems() {
        return poemRepository.findAll();
    }

    // 删除内容
    public void deletePoem(Integer id) {
        poemRepository.deleteById(id);
    }


    public List<Poem> getPoemsByUserId(Integer userId) {
        return poemRepository.findByUserId(userId);
    }

}