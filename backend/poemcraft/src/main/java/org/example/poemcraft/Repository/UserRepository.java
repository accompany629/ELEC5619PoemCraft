package org.example.poemcraft.Repository;

import org.example.poemcraft.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 自定义查询方法，比如通过用户名查询用户

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
