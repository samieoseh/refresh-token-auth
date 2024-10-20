package com.samuel.auth.controllers;

import com.samuel.auth.dto.LoginResponseDto;
import com.samuel.auth.dto.UserDto;
import com.samuel.auth.entities.UserEntity;
import com.samuel.auth.mappers.Mapper;
import com.samuel.auth.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/auth/")
public class UserCtrl {

    private UserService userService;
    private Mapper<UserEntity, UserDto> userMapper;

    public UserCtrl(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity registerUserEntity = userService.registerUser(userEntity);
            UserDto registeredUserDto  = userMapper.mapTo(registerUserEntity);
            return new ResponseEntity<>(registeredUserDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            String deviceId = userService.extractDeviceIdFromRequest(request);
            System.out.println("deviceId: " + deviceId);
            LoginResponseDto loginResponseDto = userService.login(userEntity, response);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("users")
    public ResponseEntity<?> getUsers() {
       try {
           List<UserEntity> userEntities = userService.getUsers();
           List<UserDto> userDtos = userEntities.stream()
                   .map(userMapper::mapTo)
                   .toList();
            System.out.println("UserDtos: " + userDtos);
           return new ResponseEntity<>(userDtos, HttpStatus.OK);

       } catch (Exception e) {
              return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @GetMapping("refresh")
    public ResponseEntity<?> refresh(@CookieValue(name="refreshToken", defaultValue = "default_value") String refreshToken) {
        try {
            System.out.println("refreshToken: " + refreshToken);
            if (refreshToken.equals("default_value")) {
                System.out.println("Refresh token is missing");
                return new ResponseEntity<>("Refresh token is missing", HttpStatus.FORBIDDEN);
            }
            LoginResponseDto loginResponseDto = userService.refresh(refreshToken);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/revoke/{id}")
    public ResponseEntity<?> revoke(@PathVariable String id) {
        try {
            System.out.println("id: " + id);
            userService.revoke(id);
            return new ResponseEntity<>("User refresh tokens has been revoked", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
