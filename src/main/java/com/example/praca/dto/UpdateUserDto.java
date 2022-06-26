package com.example.praca.dto;

import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class UpdateUserDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String matchingPassword;
}
