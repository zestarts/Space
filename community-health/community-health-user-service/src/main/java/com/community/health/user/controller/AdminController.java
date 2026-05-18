package com.community.health.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.health.common.dto.Result;
import com.community.health.common.dto.UserVO;
import com.community.health.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public Result<Page<UserVO>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {
        return Result.ok(userService.listUsers(page, size, keyword));
    }

    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        userService.updateUserStatus(id, body.get("status"));
        return Result.ok();
    }
}