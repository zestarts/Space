package com.community.health.user.controller;

import com.community.health.common.dto.LoginRequest;
import com.community.health.common.dto.LoginResponse;
import com.community.health.common.dto.RegisterRequest;
import com.community.health.common.dto.Result;
import com.community.health.common.dto.UserVO;
import com.community.health.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterRequest request) {
        return Result.ok(userService.register(request));
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(userService.login(request));
    }
}