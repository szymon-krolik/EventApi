package com.example.praca.repository;

import com.example.praca.model.Hobby;
import com.example.praca.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * @author Szymon Królik
 */
@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {

}
