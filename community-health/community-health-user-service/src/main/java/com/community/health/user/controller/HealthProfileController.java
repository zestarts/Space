package com.community.health.user.controller;

import com.community.health.common.dto.Result;
import com.community.health.common.entity.HealthProfile;
import com.community.health.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/health")
public class HealthProfileController {

    private final UserService userService;

    public HealthProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<HealthProfile> getHealthProfile(Authentication auth) {
        return Result.ok(userService.getHealthProfile(auth.getName()));
    }

    @PutMapping
    public Result<HealthProfile> updateHealthProfile(Authentication auth, @RequestBody HealthProfile profile) {
        return Result.ok(userService.updateHealthProfile(auth.getName(), profile));
    }
}