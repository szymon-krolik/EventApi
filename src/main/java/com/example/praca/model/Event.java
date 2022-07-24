package com.example.praca.model;

import com.google.api.client.util.DateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;

/**
 * @author Szymon Królik
 */
@Data
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String eventDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private DateTime staryDate;

    @Column
    private String city;

    @Column
    private String address;

    @Column
    private Integer maxMembers;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private User user;
}
