package com.example.praca.controller;

import com.example.praca.dto.*;
import com.example.praca.service.ReturnService;
import com.example.praca.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @GetMapping("/confirm-password")
    public ReturnService confirmNewPassword(@RequestParam("token") String token) {
        return USER_SERVICE.confirmPassword(token);
    }

    @GetMapping("/confirm-email")
    public ReturnService confirmNewEmail(@RequestParam("token") String token) {
        return USER_SERVICE.confirmMail(token);
    }

    @PutMapping("/update")
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

    @PostMapping("/login")
    public ReturnService loginUser(@RequestBody LoginUserDto dto) {
        return USER_SERVICE.loginUser(dto);
    }

    @DeleteMapping("/delete")
    public ReturnService deleteUser(@RequestParam Long userId) {
        return USER_SERVICE.deleteUser(userId);
    }

    @PutMapping("/ban-user")
    public ReturnService banUser(@RequestParam Long userId) {
        return USER_SERVICE.banUser(userId);
    }
}
