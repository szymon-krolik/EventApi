package com.example.praca.dto;

import com.example.praca.model.User;
import lombok.Data;

/**
 * @author Szymon Królik
 */
@Data
public class UserInformationDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    public static UserInformationDto of(User user) {
        UserInformationDto dto = new UserInformationDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }
}
