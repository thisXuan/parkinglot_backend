package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.entity.sign_record;
import com.parkinglot_backend.service.sign_recordService;
import com.parkinglot_backend.mapper.sign_recordMapper;
import com.parkinglot_backend.util.DateUtils;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author minxuan
* @description 针对表【sign_record】的数据库操作Service实现
* @createDate 2025-01-19 20:36:12
*/
@Service
public class sign_recordServiceImpl extends ServiceImpl<sign_recordMapper, sign_record>
    implements sign_recordService{

    @Resource
    private sign_recordMapper signRecordMapper;
    @Override
    public Result sign(String token) {
        // 解析JWT token获取用户ID
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);

        // 获取当前日期和月份
        LocalDate now = LocalDate.now();
        //Date dateMonth = Date.from(now.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        int dayOfMonth = now.getDayOfMonth();

        // 获取当前月份的最后一天
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();

        // 格式化日期
        String dateMonth = DateUtils.formatDate(lastDayOfMonth);

        sign_record signRecord = getSignRecord(userId,dateMonth);

        // 如果没有找到签到记录，创建一个新的签到记录
        if (signRecord == null) {
            signRecord = new sign_record();
            signRecord.setUserId(userId);
            signRecord.setDateMonth(dateMonth);
            signRecord.setMask(0);
            signRecord.setContinueSignMonth(0);
        }

        // 更新签到掩码和连续签到天数
        int mask = signRecord.getMask();
        mask |= (1 << dayOfMonth);
        int continueSignMonth = signRecord.getContinueSignMonth();
        if (dayOfMonth == 1 || (mask & (1 << (dayOfMonth - 1))) == 0) {
            continueSignMonth = 1;
        } else {
            continueSignMonth += 1;
        }
        //System.out.println(dayOfMonth);

        //更新对象属性
        signRecord.setMask(mask);
        signRecord.setContinueSignMonth(continueSignMonth);

        int updatedRows = signRecordMapper.updateSignRecordByUserIdAndDateMonth(userId, dateMonth, mask, continueSignMonth);

        if (updatedRows <= 0) {
            signRecordMapper.insert(signRecord);
        }
        return Result.ok("签到成功");
    }

//    @Override
//    public sign_record getSignRecord(Integer userId, Date dateMonth) {
//        return null;
//    }

    @Override
    public sign_record getSignRecord(Integer userId, String dateMonth) {
        sign_record signRecord = signRecordMapper.getSignRecordByUserIdAndDateMonth(userId, dateMonth);
        if (signRecord == null) {
            return null;
        }
        return signRecord;
    }

    @Override
    public Result getSignInDaysByUserId(String token) {
        // 解析JWT token获取用户ID
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);

        // 获取当前日期和月份
        LocalDate now = LocalDate.now();
        //Date dateMonth = Date.from(now.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        int dayOfMonth = now.getDayOfMonth();

        // 获取当前月份的最后一天
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();

        // 格式化日期
        String dateMonth = DateUtils.formatDate(lastDayOfMonth);

        sign_record signRecord = getSignRecord(userId,dateMonth);

        // 获取用户当月累计签到天数
        Long mask = signRecordMapper.getSignInDaysByUserId(userId, dateMonth);
        // 如果mask为null，返回空列表
        if (mask == null) {
            return Result.fail("从未签到");
        }
        List<Integer> signInDays = new ArrayList<>();
        // 将mask转换为二进制字符串
        String binaryMask = Long.toBinaryString(mask);
        // 从右向左解析二进制字符串
        for (int i = 0; i < binaryMask.length(); i++) {
            if (binaryMask.charAt(binaryMask.length() - 1 - i) == '1') {
                signInDays.add(i ); // day_of_month从1开始
            }
        }

        return Result.ok(signInDays);
    }

//    @Override
//    public sign_record getSignRecord(Integer userId, Date dateMonth) {
//        return signRecordMapper.selectOne(new QueryWrapper<sign_record>()
//                .eq("user_id", userId)
//                .eq("date_month", dateMonth));
//    }

}




