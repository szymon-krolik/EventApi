package com.example.praca.dto;

import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class ResendMailConfirmationDto {

    private String email;
    private String password;

}
