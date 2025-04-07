package com.parkinglot_backend.dto;

import java.util.List;
import java.util.Map;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-04-07
 * @Description:
 */


public class ReviewResponse {
    private OverallRating overallRating;
    private Map<Integer, Integer> ratingDistribution;
    private List<ReviewWithUser> reviews;

    public static class OverallRating {
        private double average;

        public OverallRating(double average) {
            this.average = average;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }
    }

    public ReviewResponse(OverallRating overallRating, Map<Integer, Integer> ratingDistribution, List<ReviewWithUser> reviews) {
        this.overallRating = overallRating;
        this.ratingDistribution = ratingDistribution;
        this.reviews = reviews;
    }

    public OverallRating getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(OverallRating overallRating) {
        this.overallRating = overallRating;
    }

    public Map<Integer, Integer> getRatingDistribution() {
        return ratingDistribution;
    }

    public void setRatingDistribution(Map<Integer, Integer> ratingDistribution) {
        this.ratingDistribution = ratingDistribution;
    }

    public List<ReviewWithUser> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewWithUser> reviews) {
        this.reviews = reviews;
    }
}
