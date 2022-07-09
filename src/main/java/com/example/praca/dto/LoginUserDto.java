package com.example.praca.dto;

import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class LoginUserDto {
    private String email;
    private String phoneNumber;
    private String password;


    public static LoginUserDto of(UpdateUserDto dto) {
        LoginUserDto user = new LoginUserDto();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }

    public static LoginUserDto of(UpdateUserPasswordDto dto) {
        LoginUserDto user = new LoginUserDto();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getCurrentPassword());

        return user;
    }
}
