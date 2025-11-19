package com.projects.services.user.controllers;

import com.projects.services.user.dtos.LoginRequestDto;
import com.projects.services.user.dtos.SignUpRequestDto;
import com.projects.services.user.dtos.UserResponseDto;
import com.projects.services.user.models.Token;
import com.projects.services.user.models.User;
import com.projects.services.user.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        User user = userService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getName(), signUpRequestDto.getPassword());
        return UserResponseDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    }
    @GetMapping("/validate/{token}")
    public UserResponseDto validateToken(@PathVariable String token){
        User user = userService.validateToken(token);
        if(user == null)
            return null;
        return UserResponseDto.from(user);
    }

}
