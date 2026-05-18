package com.community.health.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.health.common.dto.*;
import com.community.health.common.entity.HealthProfile;
import com.community.health.common.entity.Role;
import com.community.health.common.entity.User;
import com.community.health.common.enums.RoleEnum;
import com.community.health.common.exception.BusinessException;
import com.community.health.common.utils.JwtUtils;
import com.community.health.user.mapper.HealthProfileMapper;
import com.community.health.user.mapper.RoleMapper;
import com.community.health.user.mapper.UserMapper;
import com.community.health.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final HealthProfileMapper healthProfileMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserMapper userMapper, HealthProfileMapper healthProfileMapper,
                           RoleMapper roleMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.healthProfileMapper = healthProfileMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRoleCode());
        UserVO vo = toVO(user);
        return new LoginResponse(token, vo);
    }

    @Override
    @Transactional
    public UserVO register(RegisterRequest request) {
        if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())) > 0) {
            throw new BusinessException(409, "用户名已存在");
        }
        if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, request.getPhone())) > 0) {
            throw new BusinessException(409, "手机号已被注册");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRealName(request.getRealName());
        user.setRoleCode(RoleEnum.USER.getCode());
        user.setStatus(1);
        userMapper.insert(user);
        return toVO(user);
    }

    @Override
    public UserVO getProfile(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) throw new BusinessException(404, "用户不存在");
        return toVO(user);
    }

    @Override
    public UserVO updateProfile(String username, UserVO vo) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) throw new BusinessException(404, "用户不存在");
        if (vo.getPhone() != null) user.setPhone(vo.getPhone());
        if (vo.getRealName() != null) user.setRealName(vo.getRealName());
        if (vo.getAvatar() != null) user.setAvatar(vo.getAvatar());
        userMapper.updateById(user);
        return toVO(user);
    }

    @Override
    public Page<UserVO> listUsers(int page, int size, String keyword) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(User::getUsername, keyword).or().like(User::getPhone, keyword).or().like(User::getRealName, keyword));
        }
        qw.orderByDesc(User::getCreatedAt);
        Page<User> userPage = userMapper.selectPage(new Page<>(page, size), qw);
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Override
    public HealthProfile getHealthProfile(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) throw new BusinessException(404, "用户不存在");
        HealthProfile profile = healthProfileMapper.selectOne(new LambdaQueryWrapper<HealthProfile>().eq(HealthProfile::getUserId, user.getId()));
        if (profile == null) throw new BusinessException(404, "健康档案未创建");
        return profile;
    }

    @Override
    public HealthProfile updateHealthProfile(String username, HealthProfile req) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) throw new BusinessException(404, "用户不存在");
        HealthProfile profile = healthProfileMapper.selectOne(new LambdaQueryWrapper<HealthProfile>().eq(HealthProfile::getUserId, user.getId()));
        if (profile == null) {
            profile = new HealthProfile();
            profile.setUserId(user.getId());
        }
        if (req.getAge() != null) profile.setAge(req.getAge());
        if (req.getGender() != null) profile.setGender(req.getGender());
        if (req.getOccupation() != null) profile.setOccupation(req.getOccupation());
        if (req.getLivingCondition() != null) profile.setLivingCondition(req.getLivingCondition());
        if (req.getDisabilityLevel() != null) profile.setDisabilityLevel(req.getDisabilityLevel());
        if (req.getChronicDiseases() != null) profile.setChronicDiseases(req.getChronicDiseases());
        if (req.getBloodPressure() != null) profile.setBloodPressure(req.getBloodPressure());
        if (req.getBloodSugar() != null) profile.setBloodSugar(req.getBloodSugar());
        if (req.getHeartRate() != null) profile.setHeartRate(req.getHeartRate());
        if (req.getHeight() != null) profile.setHeight(req.getHeight());
        if (req.getWeight() != null) profile.setWeight(req.getWeight());
        if (profile.getId() == null) {
            healthProfileMapper.insert(profile);
        } else {
            healthProfileMapper.updateById(profile);
        }
        return profile;
    }

    private UserVO toVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setPhone(user.getPhone());
        vo.setRealName(user.getRealName());
        vo.setRoleCode(user.getRoleCode());
        vo.setStatus(user.getStatus());
        vo.setAvatar(user.getAvatar());
        vo.setCreatedAt(user.getCreatedAt());
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleCode, user.getRoleCode()));
        if (role != null) vo.setRoleName(role.getRoleName());
        return vo;
    }
}