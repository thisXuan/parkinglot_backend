package com.parkinglot_backend.service;

import com.parkinglot_backend.dto.LoginFormDTO;
import com.parkinglot_backend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.parkinglot_backend.util.Result;
import jakarta.servlet.http.HttpSession;

/**
* @author minxuan
* @description 针对表【user】的数据库操作Service
* @createDate 2024-10-17 16:37:50
*/
public interface UserService extends IService<User> {

    Result login(LoginFormDTO loginForm, HttpSession session);
}
