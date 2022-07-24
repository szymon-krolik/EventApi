package com.example.praca.controller;

import com.example.praca.dto.hobby.AddHobbyToUserDto;
import com.example.praca.dto.hobby.DeleteHobbyUserDto;
import com.example.praca.service.HobbyService;
import com.example.praca.service.ReturnService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Szymon Królik
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hobby")
public class HobbyController {

    private final HobbyService HOBBY_SERVICE;

    @PostMapping("/add")
    public ReturnService addHobbyToUser(@RequestBody AddHobbyToUserDto dto) {
        return HOBBY_SERVICE.addHobbyToUser(dto);
    }

    @PutMapping("/delete")
    public ReturnService deleteHobbyUser(@RequestBody DeleteHobbyUserDto dto) {
        return HOBBY_SERVICE.deleteHobbyUser(dto);
    }
}
