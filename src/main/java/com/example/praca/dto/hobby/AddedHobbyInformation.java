package com.example.praca.dto.hobby;

import com.example.praca.model.Hobby;
import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class AddedHobbyInformation {
    private Long id;
    private String name;

    public static AddedHobbyInformation of(Hobby hobby) {
        AddedHobbyInformation dto = new AddedHobbyInformation();

        dto.setId(hobby.getId());
        dto.setName(hobby.getName());

        return dto;
    }
}
