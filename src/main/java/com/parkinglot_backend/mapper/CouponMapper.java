package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Coupon;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CouponMapper extends BaseMapper<Coupon> {
    @Select("SELECT * FROM Coupon")
    List<Coupon> findAllCoupons();

    @Select("SELECT c.* FROM Coupon c INNER JOIN Store s ON c.store_id = s.Id WHERE s.ServiceCategory = #{serviceCategory}")
    List<Coupon> findByServiceCategory(@Param("serviceCategory") String serviceCategory);

    @Select("SELECT pay_point FROM Coupon WHERE id = #{id}")
    Integer findPayPointById(@Param("id") Integer id);

}
