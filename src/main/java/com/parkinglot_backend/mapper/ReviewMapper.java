package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.dto.RatingDistributionResult;
import com.parkinglot_backend.dto.ReviewWithUser;
import com.parkinglot_backend.entity.Review;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ReviewMapper extends BaseMapper<Review> {
    @Insert("INSERT INTO Review (user_id, rating, tags, comment, time) " +
            "VALUES (#{userId}, #{rating}, #{tags}, #{comment}, #{time})")
    int insertReview(Review review);

    @Select("SELECT AVG(rating) FROM Review")
    Double selectAverageRating();

    @Select("SELECT rating, COUNT(*) AS count FROM Review GROUP BY rating")
    List<RatingDistributionResult> selectRatingDistribution();

    @Select({"SELECT  u.name AS reviewer, r.rating, r.tags, r.comment, r.time " +
            "FROM Review r " +
            "JOIN user u ON r.user_id = u.id " +
            "ORDER BY r.time DESC"})
    List<ReviewWithUser> selectReviews();
}
