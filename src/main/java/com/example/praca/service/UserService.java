package com.example.praca.service;


import com.example.praca.dto.*;
import com.example.praca.dto.hobby.DeleteHobbyUserDto;
import com.example.praca.model.ConfirmationToken;
import com.example.praca.model.User;
import com.example.praca.model.UserRole;
import com.example.praca.repository.ConfirmationTokenRepository;
import com.example.praca.repository.HobbyRepository;
import com.example.praca.repository.UserRepository;
import com.example.praca.repository.UserRoleRepository;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultClaims;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Szymon Królik
 */

@Service
@AllArgsConstructor
//TODO USER SESSION!!
//TODO czy uzywac buildera?
@Transactional
public class UserService   {
    //TODO problem przy aktualizacji usera z tym samym email(porównanie danych i co updaterowac)
    private final UserRepository USER_REPOSITORY;
    private final HobbyRepository HOBBY_REPOSITORY;

    private final UserRoleRepository USER_ROLE_REPOSITORY;

    private final JwtTokenService JWT_TOKEN_SERVICE;
    private final String USER_NOT_EXIST_MSG = "Can't find user with %s";


    private final ConfirmationTokenRepository CONFIRMATION_TOKEN_REPOSITORY;
    private final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER;
    private final ConfirmationTokenService CONFIRMATION_TOKEN_SERVICE;
    private final EmailService EMAIL_SERVICE;
    private Map<String, String> validationError;

    public ReturnService createUser(CreateUserDto dto) {

        validationError = ValidationService.createUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        if (userExist(dto.getEmail(), dto.getPhoneNumber())) {
            validationError.put("user", "User already exist");
            return ReturnService.returnError("error", validationError, dto, 0);
        }

        dto.setPassword(encodePassword(dto.getPassword()));
        Optional<UserRole> optionalRole = USER_ROLE_REPOSITORY.findByName("UNAUTHENTICATED_USER");
        //TODO exception??
        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Brak roli");
        }
        dto.setUserRole(Collections.singletonList(optionalRole.get()));
        try {
            User user = USER_REPOSITORY.save(User.of(dto));

            final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(user);

            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
            Thread sendConfirmationThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    EMAIL_SERVICE.sendConfirmationToken(user.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken());
                }
            });

            sendConfirmationThread.start();

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
        Optional<UserRole> optionalUserRole = USER_ROLE_REPOSITORY.findByName("USER");
        if (optionalUserRole.isEmpty())
            return ReturnService.returnError("Can't find role", 0);

        /**
         * Usuwanie starej roli z listy oraz dodanie roli potwierdzonego użytkownika
         */
        List<UserRole> currentUserRoles = user.getRoles();
        currentUserRoles.removeIf(x -> x.getName().equals("UNAUTHENTICATED_USER"));

        currentUserRoles.add(optionalUserRole.get());
        user.setRoles(currentUserRoles);


        try {
            User activatedUser = USER_REPOSITORY.save(user);
            CONFIRMATION_TOKEN_REPOSITORY.deleteById(optionalConfirmationToken.get().getId());

            /**
             * Tworzenie nowego JWToken ponieważ zmieniła się rola użytkownika, która jest wykorzystywana do autoryzacji przy zapytaniach API
             */
            Claims claims = new DefaultClaims();
            List<String> authorities = user.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList());
            claims.put("authorities", authorities);
            String jwtToken = JwtTokenService.generateJwtoken(user.getEmail(), claims);
            InformationUserDto informationUserDto = InformationUserDto.of(activatedUser);
            informationUserDto.setToken(jwtToken);

            return ReturnService.returnInformation("User active", informationUserDto, 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. User activation: " + ex.getMessage(), -1);
        }
    }

    public ReturnService confirmMail(String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = CONFIRMATION_TOKEN_REPOSITORY.findConfirmationTokenByConfirmationToken(token);
        if (optionalConfirmationToken.isEmpty())
            return ReturnService.returnError("error", Collections.singletonMap("token", "Can't find token"), 0);

        User user = optionalConfirmationToken.get().getUser();
        user.setLocked(false);
        user.setEnabled(true);
        try {
            User activatedUser = USER_REPOSITORY.save(user);
            CONFIRMATION_TOKEN_REPOSITORY.deleteById(optionalConfirmationToken.get().getId());
            return ReturnService.returnInformation("User active", InformationUserDto.of(activatedUser), 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. User activation: " + ex.getMessage(), -1);
        }
    }

    public ReturnService confirmPassword(String token) {
        Optional<ConfirmationToken> optionalConfirmationToken = CONFIRMATION_TOKEN_REPOSITORY.findConfirmationTokenByConfirmationToken(token);
        if (optionalConfirmationToken.isEmpty())
            return ReturnService.returnError("error", Collections.singletonMap("token", "Can't find token"), 0);

        User user = optionalConfirmationToken.get().getUser();
        user.setLocked(false);
        user.setEnabled(true);
        try {
            User activatedUser = USER_REPOSITORY.save(user);
            CONFIRMATION_TOKEN_REPOSITORY.deleteById(optionalConfirmationToken.get().getId());
            return ReturnService.returnInformation("User active", InformationUserDto.of(activatedUser), 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. User activation: " + ex.getMessage(), -1);
        }
    }

    public ReturnService loginUser(LoginUserDto dto) {

        if (ServiceFunctions.isNull(dto.getEmail()) && ServiceFunctions.isNull(dto.getPassword()))
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please enter phone number or email"), dto, 0);

        if (!userExist(dto.getEmail(), dto.getPhoneNumber())) {
            dto.setEmail("");
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Incorrect login or password"), dto, 0);
        }
        Optional<User> optionalUser = Optional.empty();
        optionalUser = USER_REPOSITORY.findAllByEmail(dto.getEmail()).or(() -> USER_REPOSITORY.findAllByPhoneNumber(dto.getPhoneNumber()));


        if (!optionalUser.get().isEnabled()) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please active your account"), dto, 0);
        }

        if (!BCRYPT_PASSWORD_ENCODER.matches(dto.getPassword(), optionalUser.get().getPassword())) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Incorrect login or password"), dto, 0);
        }

        InformationUserDto informationUserDto = InformationUserDto.of(optionalUser.get());

        Claims claims = new DefaultClaims();
        List<String> authorities = informationUserDto.getAuthorities();
        claims.put("authorities", authorities);
        String jwtToken = JwtTokenService.generateJwtoken(informationUserDto.getEmail(), claims);
        informationUserDto.setToken(jwtToken);

        return ReturnService.returnInformation("Login success", informationUserDto, 1);
    }

    public ReturnService updateUser(UpdateUserDto dto) {

        if (!userExist(dto.getId()))
            return ReturnService.returnError("error", Collections.singletonMap("user", String.format(USER_NOT_EXIST_MSG,"")),dto, 0);
        if (!isMyProfile(dto.getId()))
            return ReturnService.returnError("Not your profile",0);


        validationError = ValidationService.updateUserValidator(dto);
        if (!validationError.isEmpty())
            return ReturnService.returnError("error", validationError, dto, 0);

        Optional<User> optionalUser = USER_REPOSITORY.findById(dto.getId());
        if (optionalUser.isEmpty())
            return ReturnService.returnError("Can't find user with given id",0);
        String oldUserEmail = optionalUser.get().getEmail();
        try {
            User updatedUser = USER_REPOSITORY.save(User.updateUser(optionalUser.get(), dto));
            if (!oldUserEmail.equals(dto.getEmail())) {
                updatedUser.setLocked(true);
                User updatedUserWithEmail = USER_REPOSITORY.save(updatedUser);
                final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(updatedUser);
                Thread sendConfirmationToken = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
                        EMAIL_SERVICE.sendConfirmationTokenChangedEmail(updatedUser.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken());
                        updatedUser.setLocked(true);
                        USER_REPOSITORY.save(updatedUser);
                    }
                });

                sendConfirmationToken.start();

                return ReturnService.returnInformation("Your email changed, please confirm",InformationUserDto.of(updatedUserWithEmail), 1);

            }
            return ReturnService.returnInformation("Succ. update user: ", InformationUserDto.of(updatedUser), 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. update user " + ex.getMessage(), -1);
        }
    }

    public ReturnService resendConfirmationToken(ResendMailConfirmationDto dto) {

        if (ServiceFunctions.isNull(dto)) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Object cannot be null"), dto, 0);
        }

        if (!ServiceFunctions.validEmail(dto.getEmail())) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Please enter correct email address"), dto, 0);
        }

        if (!userExist(dto.getEmail(), "")) {
            dto.setPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Can't find user with given email"), dto, 0);
        }

        Optional<User> optionalUser = USER_REPOSITORY.findAllByEmail(dto.getEmail());
        if (optionalUser.get().isEnabled())
            return ReturnService.returnInformation("User already enabled", 1);
        final ConfirmationToken confirmationToken = new ConfirmationToken(optionalUser.get());
        try {
            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(confirmationToken);
            Thread resendConfirmationTokenThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    EMAIL_SERVICE.sendConfirmationToken(dto.getEmail(), confirmationToken.getConfirmationToken());
                }
            });

            resendConfirmationTokenThread.start();
            return ReturnService.returnInformation("Send success", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. send confirmation token " + ex.getMessage(), -1);
        }

    }

    public ReturnService updateUserPassword(UpdateUserPasswordDto dto) {

        if (ServiceFunctions.isNull(dto.getEmail())) {
            dto.setPassword("");
            dto.setMatchingPassword("");
            dto.setCurrentPassword("");
            return ReturnService.returnError("error", Collections.singletonMap("user", "Email cannot be null"), dto, 0);
        }

        validationError = ValidationService.updateUserPasswordValidator(dto);
        if (!validationError.isEmpty()) {
            dto.setPassword("");
            dto.setMatchingPassword("");
            dto.setCurrentPassword("");
            return ReturnService.returnError("error", validationError, dto, 0);
        }

        ReturnService ret = loginUser(LoginUserDto.of(dto));
        if (ret.getStatus() != 1) {
            dto.setPassword("");
            dto.setMatchingPassword("");
            dto.setCurrentPassword("");
            return ReturnService.returnError("error", ret.getErrList(), dto, 0);
        }

        User oldUser = User.of((InformationUserDto) ret.getValue());
        oldUser.setPassword(encodePassword(dto.getPassword()));

        try {
            User user = USER_REPOSITORY.save(oldUser);
            oldUser.setLocked(true);
            USER_REPOSITORY.save(oldUser);
            final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(user);

            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
            Thread sendConfirmationThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    EMAIL_SERVICE.sendConfirmationTokenChangedPassword(user.getEmail(), CONFIRMATION_TOKEN.getConfirmationToken());
                }
            });

            sendConfirmationThread.start();
            return ReturnService.returnInformation("Confirm new password on email ", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. update user password " + ex.getMessage(), -1);
        }

    }

    public ReturnService deleteUser(Long userId) {
        Optional<User> optionalUser = USER_REPOSITORY.findById(userId);
        if (optionalUser.isEmpty()) {
            return ReturnService.returnError("Can't find user with given id",0);
        }
        if (!isMyProfile(userId))
            return ReturnService.returnError("Not your profile",0);

        try {
            USER_REPOSITORY.delete(optionalUser.get());
            HOBBY_REPOSITORY.deleteAllByUsers(optionalUser.get());
            return ReturnService.returnInformation("Succ. User deleted", 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. create user exception: " + ex.getMessage(), -1);
        }

    }

    public ReturnService banUser(Long userId) {
        Optional<User> optionalUser = USER_REPOSITORY.findById(userId);
        ReturnService ret = new ReturnService();
        if (optionalUser.isEmpty()) {
            return ReturnService.returnError("Can't find user with given id",0);
        }
        ret.setValue(optionalUser.get());
        ret.setStatus(1);

        return ret;
    }

    public boolean test(DeleteHobbyUserDto dto) {
        return ServiceFunctions.fieldIsNull(dto);
    }

    public ReturnService userProfile(String email) {
        if (ServiceFunctions.isNull(email))
            return ReturnService.returnError("Can't find user", 0);

        Optional<User> userOptional = USER_REPOSITORY.findByEmail(email);
        if (userOptional.isEmpty())
            return ReturnService.returnError("Can't find user", 0);

        InformationUserDto dto =  InformationUserDto.of(userOptional.get());
        return ReturnService.returnInformation("Succ.",dto, 1);

    }

    private  boolean isMyProfile(Long userId) {
        String token = JwtTokenService.getJwtokenFromHeader();
        Claims claims = new DefaultClaims();

        if (StringUtils.isNotBlank(token) && !token.isEmpty())
             claims = JwtTokenService.getClaimsFromJwtoken(token);

        Optional<User> user = USER_REPOSITORY.findByEmail(claims.get("sub").toString());
        if (user.isEmpty())
            return false;

        return userId == user.get().getId();

    }

    private boolean userExist(String email, String phoneNumber) {
        if (USER_REPOSITORY.findAllByEmail(email).isPresent() || USER_REPOSITORY.findAllByPhoneNumber(phoneNumber).isPresent())
            return true;

        return false;
    }

    private String encodePassword(String password) {
        return BCRYPT_PASSWORD_ENCODER.encode(password);
    }

    public boolean userExist(Long id) {
        return USER_REPOSITORY.findById(id).isPresent();
    }
}
