package com.example.praca.dto;

import com.example.praca.model.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Szymon Królik
 */
@Data
public class InformationUserDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String token;

    private List<String> authorities;

    public static InformationUserDto of(User user) {
        InformationUserDto dto = new InformationUserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAuthorities(user.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList()));

        return dto;
    }


}
