package com.example.praca.model;

import com.example.praca.dto.CreateUserDto;
import com.example.praca.dto.InformationUserDto;
import com.example.praca.dto.UpdateUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
/**
 * @author Szymon Królik
 */
@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private static final User EMPTY = new User();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Column(nullable = false)
    private boolean locked = false;

    @Column(nullable = false)
    private boolean enabled = false;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;


    public static User of(CreateUserDto dto) {
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }

    public static User of(UpdateUserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public static User of(InformationUserDto dto) {
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setId(dto.getId());

        return user;
    }

    public static User updateUser(User user, UpdateUserDto dto) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;

    }
}
