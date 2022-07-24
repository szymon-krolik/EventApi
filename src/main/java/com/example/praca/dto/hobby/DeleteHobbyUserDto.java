package com.example.praca.dto.hobby;

import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class DeleteHobbyUserDto {
    private Long userId;
    private Long hobbyId;
}
