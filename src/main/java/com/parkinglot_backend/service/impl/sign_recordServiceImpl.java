package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.entity.sign_record;
import com.parkinglot_backend.mapper.UserMapper;
import com.parkinglot_backend.service.sign_recordService;
import com.parkinglot_backend.mapper.sign_recordMapper;
import com.parkinglot_backend.util.DateUtils;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
* @author minxuan
* @description 针对表【sign_record】的数据库操作Service实现
* @createDate 2025-01-19 20:36:12
*/
@Service
public class sign_recordServiceImpl extends ServiceImpl<sign_recordMapper, sign_record >
    implements sign_recordService{

    @Resource
    private sign_recordMapper signRecordMapper;

    @Resource
    private UserMapper userMapper;

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
        int continueDaysInMonth = setContinueSign(token);
        continueSignMonth = continueDaysInMonth;


        //更新对象属性
        signRecord.setMask(mask);
        signRecord.setContinueSignMonth(continueSignMonth);

        boolean set_Point = setPoint(token);
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
        List<Integer> signInDays = signInDaysHelper(token);
        if(signInDays==null){
            return Result.fail("未获取到列表");
        }
        return Result.ok(signInDays);
    }

    @Override
    public boolean setPoint(String token){

        List<Integer> signInDays = signInDaysHelper(token);
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        Integer currentPoint = userMapper.getUserPointByUserId(userId); // 获取当前用户的成长值

        Integer lastSignInDay = Collections.max(signInDays); // 获取本月最后一次签到的日期
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);


        int additionalPoints = 0;
        if (today - lastSignInDay == 1) {
            // 如果今天日期比本月最后一次签到日期大一天，则增加10点
            additionalPoints += 10;
        } else if(today - lastSignInDay == 0){
            //今天已经重置过
            additionalPoints += 0;
        }
        else {
            // 否则增加5点
            additionalPoints += 5;
        }

        int newPoint = currentPoint + additionalPoints; // 计算新的成长值
        userMapper.updatePointByUserId(userId, newPoint); // 更新用户的成长值
        
        return true;
    }

    @Override
    public Result getPoint(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);

        int point = userMapper.getUserPointByUserId(userId);
        return Result.ok(point);
    }

    @Override
    public int setContinueSign(String token) {
        List<Integer> signInDays = signInDaysHelper(token);
        if (signInDays == null || signInDays.isEmpty()) {
            return 0;
        }

        int maxConsecutive = 1;
        int currentConsecutive = 1;
        int previousNumber = signInDays.get(0);

        for (int i = 1; i < signInDays.size(); i++) {
            int currentNumber = signInDays.get(i);

            if (currentNumber == previousNumber + 1) {
                // 当前数字与前一个数字连续
                currentConsecutive++;
            } else {
                // 当前数字与前一个数字不连续
                maxConsecutive = Math.max(maxConsecutive, currentConsecutive);
                currentConsecutive = 1;
            }

            previousNumber = currentNumber;
        }

        // 最后一个连续序列可能没有被更新到 maxConsecutive 中
        maxConsecutive = Math.max(maxConsecutive, currentConsecutive);

        return maxConsecutive;
    }


//    @Override
//    public sign_record getSignRecord(Integer userId, Date dateMonth) {
//        return signRecordMapper.selectOne(new QueryWrapper<sign_record>()
//                .eq("user_id", userId)
//                .eq("date_month", dateMonth));
//    }

    public List<Integer> signInDaysHelper(String token){
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

        if (mask == null) {
            return null;
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
        return signInDays;
    }

}




