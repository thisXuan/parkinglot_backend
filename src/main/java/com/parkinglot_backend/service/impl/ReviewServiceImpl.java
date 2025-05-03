package com.parkinglot_backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.parkinglot_backend.dto.RatingDistributionResult;
import com.parkinglot_backend.dto.ReviewDTO;
import com.parkinglot_backend.dto.ReviewResponse;
import com.parkinglot_backend.dto.ReviewWithUser;
import com.parkinglot_backend.entity.Review;
import com.parkinglot_backend.mapper.ReviewMapper;
import com.parkinglot_backend.mapper.UserMapper;
import com.parkinglot_backend.service.ReviewService;
import com.parkinglot_backend.util.JsonUtil;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static javax.swing.UIManager.put;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-04-06
 * @Description:
 */

@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private UserMapper userMapper;
    @Override
    public Result postReview(String token, ReviewDTO reviewDTO) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        if (userId == null) {
            return Result.fail("用户不存在");
        }

        Review review = new Review();

        List<String> tagsList = reviewDTO.getTags();
        //System.out.println(tagsList);
        String jsonTags = JsonUtil.listToJson(tagsList);
        //System.out.println(jsonTags);
        review.setUserId(userId);
        review.setRating(reviewDTO.getRating());
        review.setTags(jsonTags); // 将 List<String> 转换为 JSONObject
        review.setComment(reviewDTO.getComment());
        review.setTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC+16:00")))); // 使用 Timestamp 替代 Date

        int result = reviewMapper.insertReview(review);
        if (result > 0) {
            return Result.ok(true);
        } else {
            return Result.fail("提交评价失败");
        }

    }

    @Override
    public Result getReviewInfo(String token) {
        boolean tokenVa = tokenVaild(token);
        if(!tokenVa){
            return Result.fail("非管理员无查看资质");
        }
        // 获取总体评分
        Double overallRating = reviewMapper.selectAverageRating();
        // 获取评分分布
        List<RatingDistributionResult> ratingDistributionList = reviewMapper.selectRatingDistribution();
        Map<Integer, Integer> ratingDistribution = ratingDistributionList.stream()
                .collect(Collectors.toMap(
                        RatingDistributionResult::getRating,
                        RatingDistributionResult::getCount
                ));

        // 获取评价列表
        List<ReviewWithUser> reviews = reviewMapper.selectReviews();

        // 构建返回的数据结构
        ReviewResponse reviewResponse = new ReviewResponse(
                new ReviewResponse.OverallRating(overallRating),
                ratingDistribution,
                reviews
        );
        return Result.ok(reviewResponse);
    }

    private boolean tokenVaild(String token){
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        int type = userMapper.getUserTypeById(userId);
        if(type == 0){
            return false;
        }
        return  true;
    }
}
