package com.community.health.user.controller;

import com.community.health.common.dto.Result;
import com.community.health.common.dto.UserVO;
import com.community.health.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public Result<UserVO> getProfile(Authentication auth) {
        return Result.ok(userService.getProfile(auth.getName()));
    }

    @PutMapping("/profile")
    public Result<UserVO> updateProfile(Authentication auth, @RequestBody UserVO vo) {
        return Result.ok(userService.updateProfile(auth.getName(), vo));
    }
}