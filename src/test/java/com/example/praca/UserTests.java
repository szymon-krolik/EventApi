package com.example.praca;

import com.example.praca.dto.*;
import com.example.praca.service.ReturnService;
import com.example.praca.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Szymon Królik
 */
@SpringBootTest
public class UserTests {
    @Autowired
    UserService userService;


    @Test
    public void validationTest() {
        CreateUserDto dto = new CreateUserDto();
        Map<String, String> errList = new HashMap<>();
        errList.put("password", "Password should match");
        errList.put("email", "Please provide correct email address");
        errList.put("password", "Passwords should match");
        errList.put("name", "Name should have at least 3 characters");

        dto.setEmail("asd@");
        dto.setName("a");
        dto.setPassword("test1");
        dto.setMatchingPassword("asd");
        dto.setPhoneNumber("asdasd");

        ReturnService ret = userService.createUser(dto);
        Map<String, String> errListRet = ret.getErrList();

        assertEquals(0, ret.getStatus());
        assertEquals(errListRet.get("name"),errList.get("name"));
        assertEquals(errListRet.get("email"),errList.get("email"));
        assertEquals(errListRet.get("password"),errList.get("password"));
    }

    @Test
    public void nullTest() {
        CreateUserDto dto = null;
        Map<String, String> errLIst = new HashMap<>();
        errLIst.put("object", "Object cannot be null");
        ReturnService ret = userService.createUser(dto);
        Map<String, String> errListRet = ret.getErrList();
        assertEquals(0, ret.getStatus());
        assertEquals(errLIst.get("object"), errListRet.get("object"));
    }

    @Test
    public void nullTestField() {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail(null);
        dto.setPhoneNumber(null);
        dto.setPassword(null);
        dto.setMatchingPassword(null);
        dto.setName(null);

        Map<String, String> errList = new HashMap<>();
        errList.put("email", "Email cannot be null");
        errList.put("phoneNumber", "Phone number cannot be null");
        errList.put("name", "Name cannot be null");
        errList.put("password", "Password should not be empty");

        ReturnService ret = userService.createUser(dto);
        Map<String, String> errListRet = ret.getErrList();

        assertEquals(0, ret.getStatus());
        assertEquals(errList.get("name"),errListRet.get("name"));
        assertEquals(errList.get("email"),errListRet.get("email"));
        assertEquals(errList.get("password"),errListRet.get("password"));
        assertEquals(errList.get("phoneNumber"),errListRet.get("phoneNumber"));
    }

    @Test
    public void createUserTest() {
        CreateUserDto dto = new CreateUserDto();
        dto.setPassword("test1");
        dto.setMatchingPassword("test1");
        dto.setEmail("krolik.sz@wp.pl");
        dto.setPhoneNumber("+48514702363");
        dto.setName("Szymon");

        InformationUserDto dto1 = new InformationUserDto();
        dto1.setEmail("krolik.sz@wp.pl");
        dto1.setPhoneNumber("+48514702363");
        dto1.setName("Szymon");
        ReturnService ret = userService.createUser(dto);
        assertEquals(1, ret.getStatus());
        assertEquals(dto1.getEmail(), dto.getEmail());
        assertEquals(dto1.getName(), dto.getName());
        assertEquals(dto1.getPhoneNumber(), dto.getPhoneNumber());

    }

    @Test
    public void userAlreadyExistTest() {
        CreateUserDto dto = new CreateUserDto();
        dto.setPassword("test1");
        dto.setMatchingPassword("test1");
        dto.setEmail("krolik.sz@wp.pl");
        dto.setPhoneNumber("+48514702363");
        dto.setName("Szymon");

        String errMsg = "User already exist";

        ReturnService ret = userService.createUser(dto);
        assertEquals(0, ret.getStatus());
        assertEquals(errMsg,ret.getErrList().get("user"));

    }

    @Test
    public void resendEmailIncorrect() {
        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
        dto.setEmail("kro.s.pl");
        dto.setPassword("test1");

        ReturnService ret = userService.resendConfirmationToken(dto);
        assertEquals(0,ret.getStatus());
        assertEquals("Please enter correct email address", ret.getErrList().get("user"));
    }
    @Test
    public void resendEmailError() {
        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
        dto.setEmail("kro.sz@wp.pl");
        dto.setPassword("test1");

        ReturnService ret = userService.resendConfirmationToken(dto);
        assertEquals(0,ret.getStatus());
        assertEquals("Can't find user with given email", ret.getErrList().get("user"));
    }

    @Test
    public void resendEmailNull() {
        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
        dto.setEmail(null);
        dto.setPassword(null);

        ReturnService ret = userService.resendConfirmationToken(dto);
        assertEquals(0,ret.getStatus());
        assertEquals("Please enter correct email address\"", ret.getErrList().get("user"));
    }

    @Test
    public void resendEmail() {
        ResendMailConfirmationDto dto = new ResendMailConfirmationDto();
        dto.setEmail("krolik.sz@wp.pl");
        dto.setPassword("test1");

        ReturnService ret = userService.resendConfirmationToken(dto);
        assertEquals(1,ret.getStatus());
        assertEquals("Send success", ret.getMessage());
    }

    @Test
    public void userLoginValidationError() {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("");
        dto.setPassword("");
        dto.setPhoneNumber("");

        ReturnService ret = userService.loginUser(dto);
        assertEquals("Please enter phone number or email", ret.getErrList().get("user"));
        assertEquals(0, ret.getStatus());
    }

    @Test
    public void userLoginIncorrectTest() {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("kr@wp.pl");
        dto.setPassword("test1");
        dto.setPhoneNumber("");

        ReturnService ret = userService.loginUser(dto);
        assertEquals("Incorrect login or password", ret.getErrList().get("user"));
        assertEquals(0, ret.getStatus());
    }

    @Test
    public void userLoginTest() {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("krolik.sz@wp.pl");
        dto.setPassword("test1");
        dto.setPhoneNumber("");

        ReturnService ret = userService.loginUser(dto);
        InformationUserDto dtoR = (InformationUserDto)ret.getValue();
        assertEquals("Szymon", dtoR.getName());
        assertEquals("krolik.sz@wp.pl", dtoR.getEmail());
        assertEquals("+48514702363", dtoR.getPhoneNumber());
        assertEquals(1, ret.getStatus());
    }
    @Test
    public void updateUserValidationErrorTest() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setId(1L);
        dto.setPhoneNumber("+48743");
        dto.setEmail("krol.pl");
        dto.setName("Rw");
        dto.setPassword("test");

        Map<String, String> errList = new HashMap<>();
        errList.put("password", "Password should match");
        errList.put("email", "Please provide correct email address");
        errList.put("password", "Passwords should match");
        errList.put("name", "Name should have at least 3 characters");


        ReturnService ret = userService.updateUser(dto);
        Map<String, String> errListRet = ret.getErrList();

        assertEquals(0, ret.getStatus());
        assertEquals(errList.get("name"),errListRet.get("name"));
        assertEquals(errList.get("email"),errListRet.get("email"));
        assertEquals(errList.get("password"),errListRet.get("password"));
        assertEquals(errList.get("phoneNumber"),errListRet.get("phoneNumber"));

    }
    @Test
    public void updateUserErrorTest() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setId(3L);
        dto.setPhoneNumber("+48741852963");
        dto.setEmail("krolik.sz@wp.pl");
        dto.setName("Radosław");
        dto.setPassword("test");

        ReturnService ret = userService.updateUser(dto);
        assertEquals(0,ret.getStatus());
        assertEquals("Can't find user with given id",ret.getErrList().get("user"));

    }

    @Test
    public void updateUserTest() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setId(8L);
        dto.setPhoneNumber("+48741852963");
        dto.setEmail("mentalnyyy@gmail.com");
        dto.setName("Radosła");

        InformationUserDto retDto = new InformationUserDto();
        retDto.setId(8L);
        retDto.setPhoneNumber("+48741852963");
        retDto.setEmail("mentalnyyy@gmail.com");
        retDto.setName("Radosław");

        ReturnService ret = userService.updateUser(dto);
        assertEquals(1,ret.getStatus());
        assertEquals("Succ. update user:",ret.getMessage());

    }



}
