package com.example.praca.repository;

import com.example.praca.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Szymon Królik
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findAllByEmail(String email);
    Optional<User> findAllByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);
}
