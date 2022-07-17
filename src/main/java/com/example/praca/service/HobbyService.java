package com.example.praca.service;

import com.example.praca.dto.InformationUserDto;
import com.example.praca.dto.hobby.AddHobbyToUserDto;
import com.example.praca.model.Hobby;
import com.example.praca.model.User;
import com.example.praca.repository.HobbyRepository;
import com.example.praca.repository.UserRepository;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author Szymon Królik
 */
@Service
@AllArgsConstructor
public class HobbyService {
    //TODO delete hobby
    private final HobbyRepository HOBBY_REPOSITORY;
    private final UserRepository USER_REPOSITORY;
    private final UserService USER_SERVICE;
    private Map<String, String> validationError;

    // TODO return succ
    public ReturnService addHobbyToUser(AddHobbyToUserDto dto) {
        List<Hobby> hobbyList = new ArrayList<>();
        List<Hobby> userHobbiesList = new ArrayList<>();

        validationError = ValidationService.addHobbyToUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        if (!USER_SERVICE.userExist(dto.getUserId())) {
            validationError.put("user", "Can't find user with given id");
            return ReturnService.returnError("error", validationError, dto, 0);
        }

        for (Long id : dto.getHobbies()) {
                if (!hobbyExist(id)) {
                    validationError.put("hobby", "Can't find hobby with given id");
                    return ReturnService.returnError("error", validationError, dto, 0);
                }
        }

        User user = USER_REPOSITORY.findById(dto.getUserId()).get();
        userHobbiesList = user.getHobbies();

        hobbyList = HOBBY_REPOSITORY.findAllById(dto.getHobbies());

        hobbyList.removeAll(new HashSet(userHobbiesList));

        //ADd new hobby for user
        userHobbiesList.addAll(hobbyList);

        try {

            user.setHobbies(userHobbiesList);
            USER_REPOSITORY.save(user);
            return ReturnService.returnInformation("Succ. hobby added", dto, 1);
        }catch (Exception ex) {
            return ReturnService.returnError("Err. add hobby to user exception: " + ex.getMessage(), -1);
        }



    }


    @Transactional
    public boolean hobbyExist(Long id) {
        return HOBBY_REPOSITORY.findById(id).isPresent();
    }

}
