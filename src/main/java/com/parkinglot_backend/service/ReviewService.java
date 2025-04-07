package com.parkinglot_backend.service;

import com.parkinglot_backend.dto.ReviewDTO;
import com.parkinglot_backend.util.Result;

public interface ReviewService {
    Result postReview(String token, ReviewDTO reviewDTO);

    Result getReviewInfo(String token);
}
