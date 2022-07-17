package com.example.praca.controller;

import com.example.praca.dto.hobby.AddHobbyToUserDto;
import com.example.praca.service.HobbyService;
import com.example.praca.service.ReturnService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Szymon Królik
 */
@RestController
@AllArgsConstructor
public class HobbyController {

    private final HobbyService HOBBY_SERVICE;

    @PostMapping("/hobby/add")
    public ReturnService addHobbyToUser(@RequestBody AddHobbyToUserDto dto) {
        return HOBBY_SERVICE.addHobbyToUser(dto);
    }
}
