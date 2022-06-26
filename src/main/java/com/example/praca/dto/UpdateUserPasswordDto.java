package com.example.praca.dto;

import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class UpdateUserPasswordDto {
    private String email;
    private Long id;
    private String password;
    private String matchingPassword;
}
