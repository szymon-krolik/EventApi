package com.example.praca.model;

import com.example.praca.dto.CreateUserDto;
import com.example.praca.dto.InformationUserDto;
import com.example.praca.dto.UpdateUserDto;
import com.example.praca.dto.hobby.AddHobbyToUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Szymon Królik
 */
@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

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
    private boolean locked = false;

    @Column(nullable = false)
    private boolean enabled = false;

    @CreatedBy
    private String createdBy;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
            @JoinTable(
                    name = "user_hobbies",
                    joinColumns = @JoinColumn(name = "user_id"),
                    inverseJoinColumns = @JoinColumn(name = "hobby_id")
            )
    List<Hobby> hobbies;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @OneToMany(mappedBy="user")
    private List<Event> events;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<UserRole> roles;

    public static User of(CreateUserDto dto) {
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRoles(dto.getUserRole());

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
