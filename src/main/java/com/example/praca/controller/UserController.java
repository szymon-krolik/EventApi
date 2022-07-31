package com.example.praca.controller;

import com.example.praca.dto.*;
import com.example.praca.model.User;
import com.example.praca.repository.UserRepository;
import com.example.praca.service.ReturnService;
import com.example.praca.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Szymon Królik
 */
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService USER_SERVICE;
    private final UserRepository userRepository;

    @PostMapping
    public ReturnService createUser(@RequestBody CreateUserDto dto) {
        return USER_SERVICE.createUser(dto);
    }

    /**
     *
     * @param token
     * @return
     * Potwierdzenie użytkownika po rejestracji
     */
    @GetMapping("/confirm")
    public ReturnService confirmUser(@RequestParam("token") String token) {
        return USER_SERVICE.confirmUser(token);
    }

    /**
     * Potwierdzenie nowego email po zmiaaniu
     * @param token
     * @return
     */
    @GetMapping("/confirm-email")
    public ReturnService confirmNewEmail(@RequestParam("token") String token) {
        return USER_SERVICE.confirmMail(token);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping("/update")
    public ReturnService updateUser(@RequestBody UpdateUserDto dto) {
        return USER_SERVICE.updateUser(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PutMapping("/change-password")
    public ReturnService updateUserPassword(@RequestBody UpdateUserPasswordDto dto) {
        return USER_SERVICE.updateUserPassword(dto);
    }


    /**
     * Potwierdzenie nowego hasła po zmianie
     * @param token
     * @return
     */
    @GetMapping("/confirm-password")
    public ReturnService confirmNewPassword(@RequestParam("token") String token) {
        return USER_SERVICE.confirmPassword(token);
    }


    /**
     * Ponowna wysyłka email z potwierdzeniem utworzenia konta
     * @param dto
     * @return
     */
    @PostMapping("/resend-mail")
    public ReturnService resendMail(@RequestBody ResendMailConfirmationDto dto) {
        return USER_SERVICE.resendConfirmationToken(dto);
    }

    @PostMapping("/login")
    public ReturnService loginUser(@RequestBody LoginUserDto dto) {
        return USER_SERVICE.loginUser(dto);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @DeleteMapping("/delete")
    public ReturnService deleteUser(@RequestParam Long userId) {
        return USER_SERVICE.deleteUser(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/ban-user")
    public ReturnService banUser(@RequestParam Long userId) {
        return USER_SERVICE.banUser(userId);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @GetMapping("")
    public ReturnService getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return USER_SERVICE.userProfile(email);

    }





}


