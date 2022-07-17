package com.example.praca.dto.hobby;

import lombok.Data;

import java.util.List;

/**
 * @author Szymon Królik
 */
@Data
public class AddHobbyToUserDto {
    private Long userId;
    private List<Long> hobbies;
}
