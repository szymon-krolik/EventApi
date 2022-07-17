package com.example.praca.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Szymon Królik
 */
@Data
@Entity
@Table(name = "hobby")
public class Hobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "hobbies", fetch = FetchType.EAGER)
    Set<User> users;

    @Override
    public String toString() {
        return name;
    }
}
