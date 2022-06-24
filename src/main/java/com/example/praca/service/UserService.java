package com.example.praca.service;


import com.example.praca.dto.CreateUserDto;
import com.example.praca.dto.UserInformationDto;
import com.example.praca.model.User;
import com.example.praca.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Szymon Królik
 */
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository USER_REPOSITORY;
    private final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER;
    private Map<String, String> validationError = new HashMap<>();

    public ReturnService createUser(CreateUserDto dto) {
        validationError.clear();

        validationError = ValidationService.createUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("Validation errors", validationError, dto, 0);

        if (userExist(dto.getEmail(), dto.getPhoneNumber())) {
            validationError.put("object", "User already exist");
            return ReturnService.returnError("Validation errors", validationError, dto, 0);
        }


        final String ENCODE_PASSWORD = BCRYPT_PASSWORD_ENCODER.encode(dto.getPassword());
        dto.setPassword(ENCODE_PASSWORD);
        try {
            User user = USER_REPOSITORY.save(User.of(dto));
            return ReturnService.returnInformation("Succ. user created", UserInformationDto.of(user), 1);
        } catch (Exception e) {
            return ReturnService.returnError("Err. create user exception: " + e.getMessage(),-1);
        }

    }

    private boolean userExist(String email, String phoneNumber) {
        if (USER_REPOSITORY.findAllByEmail(email).isPresent() || USER_REPOSITORY.findAllByPhoneNumber(phoneNumber).isPresent())
            return true;

        return false;
    }

}
