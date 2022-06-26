package com.example.praca.service;

import com.example.praca.dto.CreateUserDto;
import com.example.praca.dto.UpdateUserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Szymon Królik
 */
@Service
@AllArgsConstructor
public class ValidationService {
    private static Map<String, String> errList = new HashMap<>();
    private final static int MIN_LENGTH = 3;

    public static Map<String, String> createUserValidator(CreateUserDto dto) {
        errList.clear();
        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (!ServiceFunctions.isNull(dto.getEmail())) {
            if (!ServiceFunctions.validEmail(dto.getEmail()))
                errList.put("email", "Please provide correct email address");
        } else {
            errList.put("email", "Email cannot be null");
        }

        if (!ServiceFunctions.isNull(dto.getPhoneNumber())) {
            if (!ServiceFunctions.validPhoneNumber(dto.getPhoneNumber()))
                errList.put("phoneNumber", "Please provide correct phone number");
        } else {
            errList.put("phoneNumber", "Phone number cannot be null");
        }

        if (ServiceFunctions.isNull(dto.getName())) {
            errList.put("name", "Name cannot be null");
        } else if (dto.getName().length() < MIN_LENGTH) {
            errList.put("name", "Name should have at least 3 characters");
        }

        if (ServiceFunctions.isNull(dto.getPassword()))
            errList.put("password", "Password should not be empty");

        if (ServiceFunctions.isNull(dto.getMatchingPassword()))
            errList.put("matchingPassword", "Matching password should not be empty");

        if (!ServiceFunctions.isNull(dto.getPassword()) && !ServiceFunctions.isNull(dto.getMatchingPassword())) {
            if (!dto.getPassword().toLowerCase(Locale.ROOT).equals(dto.getMatchingPassword().toLowerCase()))
                errList.put("password", "Passwords should match");
        }

        return errList;
    }

    public static Map<String, String> updateUserValidator(UpdateUserDto dto) {
        errList.clear();
        if (ServiceFunctions.isNull(dto))
            return Collections.singletonMap("object", "Object cannot be null");

        if (!ServiceFunctions.isNull(dto.getEmail())) {
            if (!ServiceFunctions.validEmail(dto.getEmail()))
                errList.put("email", "Please provide correct email address");
        } else {
            errList.put("email", "Email cannot be null");
        }

        if (!ServiceFunctions.isNull(dto.getPhoneNumber())) {
            if (!ServiceFunctions.validPhoneNumber(dto.getPhoneNumber()))
                errList.put("phoneNumber", "Please provide correct phone number");
        } else {
            errList.put("phoneNumber", "Phone number cannot be null");
        }

        if (ServiceFunctions.isNull(dto.getName())) {
            errList.put("name", "Name cannot be null");
        } else if (dto.getName().length() < MIN_LENGTH) {
            errList.put("name", "Name should have at least 3 characters");
        }


        return errList;
    }

}
