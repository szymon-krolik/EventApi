package com.example.praca.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Szymon Królik
 */
public class ServiceFunctions<T> {
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final String PHONE_NUMBER_REGEX = "(0|91)?[7-9][0-9]{9}";

    public static boolean validEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    public static boolean validPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.find();
    }

    public static boolean isNull(String text) {
        if (text == null)
            return true;
        return text.isBlank() || text.isEmpty();
    }

//    public static <T> boolean isNull(T obj) {
//        return Arrays.stream(obj.getClass().getFields()).anyMatch(Objects::isNull);
//    }

    public static <T> boolean isNull(T obj) {
        return obj == null;
    }


}
