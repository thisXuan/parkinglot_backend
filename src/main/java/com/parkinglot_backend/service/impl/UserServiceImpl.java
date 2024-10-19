package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.dto.LoginFormDTO;
import com.parkinglot_backend.dto.RegisterDTO;
import com.parkinglot_backend.entity.User;
import com.parkinglot_backend.service.UserService;
import com.parkinglot_backend.mapper.UserMapper;
import com.parkinglot_backend.service.VerificationCodeService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author minxuan
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-10-17 16:37:50
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private VerificationCodeService verificationCodeService; // 注入验证码服务

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1. 校验数据库中是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, loginForm.getName());
        User user = getOne(queryWrapper);
        // 2. 不存在，登录失败
        if (user == null) {
            return Result.fail("用户不存在!");
        }
        // 3，存在，校验密码
        if(!user.getPassword().equals(loginForm.getPassword())) {
            return Result.fail("密码错误！");
        }
        // 4. 如果密码正确，返回jwt token
        Map<String, Object> claims = new HashMap<>();
        claims.put("UserId", user.getId());
        String jwt = JwtUtils.generateJwt(claims);
        return Result.ok(jwt);
    }

    @Override
    public Result logout(HttpSession session) {
        return Result.ok("退出成功");
    }

    //注册
    @Override
    public Result register(RegisterDTO registerDTO) {
        // 验证手机号格式
        if (!registerDTO.getPhone().matches("\\d{11}")) {
            return Result.fail("手机号格式不正确，必须为11位数字");
        }

        // 验证验证码
        if (!verificationCodeService.validateCode(registerDTO.getCaptcha())) {
            return Result.fail("验证码错误或已过期");
        }

        // 1. 校验数据库中是否存在相同用户名或手机号
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, registerDTO.getUsername())
                .or().eq(User::getPhone, registerDTO.getPhone());
        User user = getOne(queryWrapper);
        // 2. 如果存在，注册失败
        if (user != null) {
            return Result.fail("用户名或手机号已存在!");
        }
        // 3. 不存在，保存新用户
        user = new User();
        user.setName(registerDTO.getUsername());
        user.setPhone(registerDTO.getPhone());
        user.setPassword(registerDTO.getPassword()); // 这里应该使用加密密码
        boolean saved = save(user);
        // 4. 保存失败，注册失败
        if (!saved) {
            return Result.fail("注册失败");
        }
        // 5. 保存成功，返回成功信息
        return Result.ok("注册成功");
    }
}



