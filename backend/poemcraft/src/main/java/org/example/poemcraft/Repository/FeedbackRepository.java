package org.example.poemcraft.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.poemcraft.Entity.Feedback;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
