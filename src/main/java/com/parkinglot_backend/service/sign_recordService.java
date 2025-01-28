package com.parkinglot_backend.service;

import com.parkinglot_backend.entity.sign_record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.parkinglot_backend.util.Result;

import java.util.Date;

/**
* @author minxuan
* @description 针对表【sign_record】的数据库操作Service
* @createDate 2025-01-19 20:36:12
*/
public interface sign_recordService extends IService<sign_record> {

    Result sign(String token);

  //  sign_record getSignRecord(Integer userId, Date dateMonth);

    sign_record getSignRecord(Integer userId, String dateMonth);

    Result getSignInDaysByUserId(String token);

    boolean setPoint(String token);

    Result getPoint(String token);

    int setContinueSign(String token);

}
