package com.example.praca.dto;

import com.example.praca.model.UserRole;
import lombok.Data;

import java.util.List;

/**
 * @author Szymon Królik
 */
@Data
public class CreateUserDto {
    private String name;
    private String email;
    private String password;
    private String matchingPassword;
    private String phoneNumber;
    private List<UserRole> userRole;

    public static CreateUserDto of(UpdateUserDto dto) {
        CreateUserDto user = new CreateUserDto();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setMatchingPassword(dto.getMatchingPassword());
        user.setPassword(dto.getPassword());

        return user;
    }

}

