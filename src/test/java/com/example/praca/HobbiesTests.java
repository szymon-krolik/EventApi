package com.example.praca;

import com.example.praca.dto.hobby.AddHobbyToUserDto;
import com.example.praca.service.HobbyService;
import com.example.praca.service.ReturnService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Szymon Królik
 */
@SpringBootTest
public class HobbiesTests {

//    @Autowired
//    HobbyService hobbyService;
//
//    @Test
//    public void nullTest() {
//        List<Long> testList = new ArrayList<>(Arrays.asList(null));
//        AddHobbyToUserDto dto = new AddHobbyToUserDto();
//        dto.setHobbies(testList);
//        dto.setUserId(5L);
//
//        ReturnService ret = hobbyService.addHobbyToUser(dto);
//        assertEquals(0, ret.getStatus());
//        assertEquals("Object cannot be null", ret.getErrList().get("object"));
//
//    }
//
////    "user", "Can't find user with given email"
//    @Test
//    public void cantFindHobbyTest() {
//        List<Long> testList = new ArrayList<>(Arrays.asList(99L));
//        AddHobbyToUserDto dto = new AddHobbyToUserDto();
//        dto.setHobbies(testList);
//        dto.setUserId(5L);
//
//        ReturnService ret = hobbyService.addHobbyToUser(dto);
//        assertEquals(0, ret.getStatus());
//        assertEquals("Can't find hobby with given id", ret.getErrList().get("hobby"));
//    }
//
//    @Test
//    public void cantFindUserTest() {
//        List<Long> testList = new ArrayList<>(Arrays.asList(99L));
//        AddHobbyToUserDto dto = new AddHobbyToUserDto();
//        dto.setUserId(99L);
//        dto.setHobbies(testList);
//
//        ReturnService ret = hobbyService.addHobbyToUser(dto);
//        assertEquals(0, ret.getStatus());
//        assertEquals("Can't find user with given id", ret.getErrList().get("user"));
//    }
//
//    @Test
//    public void addHobbyToUserTest() {
//        List<Long> testList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
//        AddHobbyToUserDto dto = new AddHobbyToUserDto();
//        dto.setUserId(5L);
//        dto.setHobbies(testList);
//
//        ReturnService ret = hobbyService.addHobbyToUser(dto);
//        assertEquals(1, ret.getStatus());
//
//    }
}
