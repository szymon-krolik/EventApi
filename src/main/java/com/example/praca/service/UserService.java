package com.example.praca.service;


import com.example.praca.dto.*;
import com.example.praca.model.ConfirmationToken;
import com.example.praca.model.User;
import com.example.praca.repository.ConfirmationTokenRepository;
import com.example.praca.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Szymon Królik
 */
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository USER_REPOSITORY;

    private final ConfirmationTokenRepository CONFIRMATION_TOKEN_REPOSITORY;
    private final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER;
    private final ConfirmationTokenService CONFIRMATION_TOKEN_SERVICE;
    private final EmailService EMAIL_SERVICE;
    private Map<String, String> validationError = new HashMap<>();

    //TODO change password method
    public ReturnService createUser(CreateUserDto dto) {
        validationError.clear();

        validationError = ValidationService.createUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        if (userExist(dto.getEmail(), dto.getPhoneNumber())) {
            validationError.put("user", "User already exist");
            return ReturnService.returnError("error", validationError, dto, 0);
        }

        dto.setPassword(encodePassword(dto.getPassword()));

        try {
            User user = USER_REPOSITORY.save(User.of(dto));
            final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(user);

            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
            sendConfirmationToken(user.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken());

            return ReturnService.returnInformation("Succ. user created", InformationUserDto.of(user), 1);
        } catch (Exception e) {
            return ReturnService.returnError("Err. create user exception: " + e.getMessage(), -1);
        }

    }

    public ReturnService confirmUser(String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = CONFIRMATION_TOKEN_REPOSITORY.findConfirmationTokenByConfirmationToken(token);
        if (optionalConfirmationToken.isEmpty())
            return ReturnService.returnError("error", Collections.singletonMap("token", "Can't find token"), 0);

        User user = optionalConfirmationToken.get().getUser();
        user.setEnabled(true);
        try {
            User activatedUser = USER_REPOSITORY.save(user);
            return ReturnService.returnInformation("User active", InformationUserDto.of(activatedUser), 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. User activation: " + ex.getMessage(), -1);
        }
    }

    public ReturnService loginUser(LoginUserDto dto) {

        if (ServiceFunctions.isNull(dto.getEmail()) || ServiceFunctions.isNull(dto.getPassword()))
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please enter phone number or email"), dto, 0);

        if (!userExist(dto.getEmail(), dto.getPhoneNumber())) {
            dto.setEmail("");
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Incorrect login or password"), dto, 0);
        }
        Optional<User> optionalUser = Optional.empty();
        optionalUser = USER_REPOSITORY.findAllByEmail(dto.getEmail()).or(() -> USER_REPOSITORY.findAllByPhoneNumber(dto.getPhoneNumber()));


        if (optionalUser.get().isEnabled()) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please active your account"), dto, 0);
        }

        if (!BCRYPT_PASSWORD_ENCODER.matches(dto.getPassword(), optionalUser.get().getPassword())) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Incorrect login or password"), dto, 0);
        }

        InformationUserDto informationUserDto = InformationUserDto.of(optionalUser.get());
        return ReturnService.returnInformation("Login success", informationUserDto, 1);
    }


    //TODO confirm mail if mail was changed
    public ReturnService updateUser(UpdateUserDto dto) {
        validationError.clear();

        if (!userExist(dto.getEmail(), dto.getPhoneNumber()))
            return ReturnService.returnError("error", Collections.singletonMap("user", "Can't find user with given email or phone number"), dto, 0);
        validationError = ValidationService.updateUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError,dto, 0);

        Optional<User> optionalUser = USER_REPOSITORY.findById(dto.getId());
        try {
            User updatedUser = USER_REPOSITORY.save(User.updateUser(optionalUser.get(), dto));
            if (!optionalUser.get().getEmail().equals(dto.getEmail())) {
                final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(updatedUser);

                CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
                sendConfirmationToken(updatedUser.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken());
                updatedUser.setEnabled(false);
                USER_REPOSITORY.save(updatedUser);
                return ReturnService.returnInformation("Succ. update user: ", 1);

            }
            return ReturnService.returnInformation("Succ. update user: ", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. update user " + ex.getMessage(), -1);
        }
    }

    private boolean userExist(String email, String phoneNumber) {
        if (USER_REPOSITORY.findAllByEmail(email).isPresent() || USER_REPOSITORY.findAllByPhoneNumber(phoneNumber).isPresent())
            return true;

        return false;
    }

    private String encodePassword(String password) {
        return BCRYPT_PASSWORD_ENCODER.encode(password);
    }

    private void sendConfirmationToken(String userMail, String token) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String activationLink = "http://127.0.0.1:8080/user/confirm?token=" + token;
        String mailMessage = "Dziękujemy za rejestracje, proszę kliknąć w link aby aktywować konto \n";
        simpleMailMessage.setTo(userMail);
        simpleMailMessage.setSubject("Mail confirmation");
        simpleMailMessage.setFrom("<MAIL>");
        simpleMailMessage.setText(mailMessage + activationLink);
        EMAIL_SERVICE.sendEmail(simpleMailMessage);

    }

    public ReturnService resendConfirmationToken(ResendMailConfirmationDto dto) {
        validationError.clear();

        if (ServiceFunctions.isNull(dto)) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Object cannot be null"),dto, 0);
        }

        if (!ServiceFunctions.validEmail(dto.getEmail())) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please enter correct email address"),dto, 0);
        }

        if (!userExist(dto.getEmail(), "")) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Can't find user with given email"), dto,0);
        }

        Optional<User> optionalUser = USER_REPOSITORY.findAllByEmail(dto.getEmail());
        if (optionalUser.get().isEnabled())
            return ReturnService.returnInformation("User already enabled",1);
        final ConfirmationToken confirmationToken = new ConfirmationToken(optionalUser.get());
        try {
            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(confirmationToken);
            sendConfirmationToken(dto.getEmail(), confirmationToken.getConfirmationToken());
            return ReturnService.returnInformation("Send success",1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. send confirmation token " + ex.getMessage(), -1);
        }

    }

    public ReturnService updateUserPassword(UpdateUserPasswordDto dto) {
        return new ReturnService();
    }

}
