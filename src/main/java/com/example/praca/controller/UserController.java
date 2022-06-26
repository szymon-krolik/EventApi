package com.example.praca.controller;

import com.example.praca.dto.CreateUserDto;
import com.example.praca.dto.ResendMailConfirmationDto;
import com.example.praca.dto.UpdateUserDto;
import com.example.praca.dto.UpdateUserPasswordDto;
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

    @GetMapping("/confirm")
    public ReturnService confirmUser(@RequestParam("token") String token) {
        return USER_SERVICE.confirmUser(token);
    }

    @PutMapping
    public ReturnService updateUser(@RequestBody UpdateUserDto dto) {
        return USER_SERVICE.updateUser(dto);
    }

    @PutMapping("/change-password")
    public ReturnService updateUserPassword(@RequestBody UpdateUserPasswordDto dto) {
        return USER_SERVICE.updateUserPassword(dto);
    }
    @PostMapping("/resend-mail")
    public ReturnService resendMail(@RequestBody ResendMailConfirmationDto dto) {
        return USER_SERVICE.resendConfirmationToken(dto);
    }
}
