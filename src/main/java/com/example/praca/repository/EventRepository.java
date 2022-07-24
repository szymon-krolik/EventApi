package com.example.praca.repository;

import com.example.praca.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Szymon Królik
 */
public interface EventRepository extends JpaRepository<Event, Long> {
}
