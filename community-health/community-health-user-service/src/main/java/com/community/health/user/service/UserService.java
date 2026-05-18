package com.community.health.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.health.common.dto.*;
import com.community.health.common.entity.HealthProfile;

public interface UserService {
    LoginResponse login(LoginRequest request);
    UserVO register(RegisterRequest request);
    UserVO getProfile(String username);
    UserVO updateProfile(String username, UserVO vo);
    Page<UserVO> listUsers(int page, int size, String keyword);
    void updateUserStatus(Long userId, Integer status);
    HealthProfile getHealthProfile(String username);
    HealthProfile updateHealthProfile(String username, HealthProfile profile);
}