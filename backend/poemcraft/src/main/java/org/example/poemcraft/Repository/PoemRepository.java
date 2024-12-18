package org.example.poemcraft.Repository;

import org.example.poemcraft.Entity.Poem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PoemRepository extends JpaRepository<Poem, Integer> {
    // You can add custom queries here if needed
    List<Poem> findByUserId(Integer userId);
}
