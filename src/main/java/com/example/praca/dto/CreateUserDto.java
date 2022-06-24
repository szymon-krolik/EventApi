package com.example.praca.dto;

import lombok.Data;

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

}

