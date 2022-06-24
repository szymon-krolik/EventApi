package com.example.praca.controller;

import com.example.praca.dto.CreateUserDto;
import com.example.praca.service.ReturnService;
import com.example.praca.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Szymon Królik
 */
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService USER_SERVICE;

    @PostMapping
    public ReturnService createUser(@RequestBody CreateUserDto dto) {
        return USER_SERVICE.createUser(dto);
    }
}
