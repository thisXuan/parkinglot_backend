package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.dto.*;
import com.parkinglot_backend.entity.User;
import com.parkinglot_backend.service.UserService;
import com.parkinglot_backend.mapper.UserMapper;
import com.parkinglot_backend.service.VerificationCodeService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private UserMapper userMapper;

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1. 校验数据库中是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, loginForm.getPhone());
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
        // type 0为普通用户，1为管理员
        Map<String, Object> claims = new HashMap<>();
        claims.put("UserId", user.getId());
        int type = userMapper.getUserTypeById(user.getId());
        String jwt = JwtUtils.generateJwt(claims);
        UserDTO userDTO = new UserDTO(jwt,type);
        System.out.println(userDTO);
        return Result.ok(userDTO);
    }

    @Override
    public Result logout(HttpSession session) {
        return Result.ok("退出成功");
    }

    /**
     * 重置密码
     * @param registerDTO
     * @return
     */
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
        // 这里应该使用加密密码
        user.setPassword(encryptPassword(registerDTO.getPassword()));
        user.setCreate_time(LocalDateTime.now(ZoneId.of("UTC+08:00")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        try {
            save(user);
        } catch (Exception e) {
            // 4. 保存失败，注册失败
            if (e instanceof DataIntegrityViolationException) {
                // 处理唯一性约束违反异常
                return Result.fail("用户名或手机号已存在!");
            } else {
                return Result.fail("注册失败");
            }
        }
        // 5. 保存成功，返回成功信息
        return Result.ok("注册成功");
    }

    /**
     * 重置密码
     * @param forgetPasswordDTO
     * @return
     */
    @Override
    public Result resetPassword(ForgetPasswordDTO forgetPasswordDTO) {
        // 验证手机号格式
        if (!forgetPasswordDTO.getPhone().matches("\\d{11}")) {
            return Result.fail("手机号格式不正确，必须为11位数字");
        }

        // 验证验证码
        if (!verificationCodeService.validateCode(forgetPasswordDTO.getCaptcha())) {
            return Result.fail("验证码错误或已过期");
        }

        // 校验数据库中是否存在该手机号
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, forgetPasswordDTO.getPhone());
        User user = getOne(queryWrapper);
        // 不存在该手机号
        System.out.println(user);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        System.out.println(forgetPasswordDTO);
        // 更新密码（这里应该使用加密密码）
        // 假设我们使用了一个密码加密的方法 encryptPassword
        String encryptedPassword = encryptPassword(forgetPasswordDTO.getNewPassword());

        user.setPassword(encryptedPassword);
        boolean updated = updateById(user);
        if (!updated) {
            return Result.fail("密码重置失败");
        }

        // 清除该手机号对应的验证码（如果存储了）
        verificationCodeService.removeVerificationCode(forgetPasswordDTO.getPhone());

        return Result.ok("密码重置成功");
    }

    @Override
    public Result getUserInfo(String phone) {
        User user = query().eq("phone", phone).one();
        if (user == null) {
            return Result.fail("用户不存在！");
        }
        return Result.ok(user);
    }

    @Override
    public Result getUserRole(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        User user = query().eq("id", userId).one();
        if(user==null){
            return Result.fail("用户不存在");
        }
        return Result.ok(user.getType());
    }

    @Override
    public Result getUsers(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        User u = query().eq("id", userId).one();
        if(u.getType()==0){
            return Result.fail("没有权限");
        }
        List<User> list = query().list();
        List<UserMessageDTO> dtoList = list.stream()
                .map(user -> new UserMessageDTO(
                        user.getId(),
                        user.getName(),
                        user.getPhone(),
                        user.getPoint(),
                        user.getType()))
                .collect(Collectors.toList());
        return Result.ok(dtoList);
    }

    @Override
    public Result updateUsers(String token,User user) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        int type = userMapper.getUserTypeById(userId);
        if(type == 0){
            return Result.fail("非管理员无修改资质");
        }
        // 获取user的id
        Integer userIdToUpdate = user.getId();
        if (userIdToUpdate == null) {
            return Result.fail("User id 不存在");
        }

        // 构造更新条件
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userIdToUpdate);

        // 将user对象的id设置为null，避免更新id字段
        user.setId(null);

        // 更新数据库中对应行的其他内容
        int updateRows = userMapper.update(user, updateWrapper);
        if (updateRows > 0) {
            return Result.ok("用户更新成功");
        } else {
            return Result.fail("用户更新失败");
        }
    }

    /**
     * 加密密码
     * @param password
     * @return
     */
    private String encryptPassword(String password) {
        // 使用强哈希算法加密密码，例如 BCryptPasswordEncoder
        // return new BCryptPasswordEncoder().encode(password);

        return password; // 示例中直接返回密码，实际应用中应加密
    }


}




