package com.simplescrumpoker.repository;

import com.simplescrumpoker.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    boolean existsById(Long userId);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default Optional<User> findByUsername(String username) {
        return findByEmail(username);
    }

}
