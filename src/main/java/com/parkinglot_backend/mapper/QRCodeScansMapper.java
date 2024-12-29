package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Points;
import com.parkinglot_backend.entity.QRCodeScans;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface QRCodeScansMapper extends BaseMapper<QRCodeScans> {
    @Update("UPDATE QRCodeScans SET qr_content = #{qrContent}, scan_time = NOW() WHERE user_id = #{userId}")
    int updateQRCodeContentByUserId(@Param("userId") Integer userId, @Param("qrContent") String qrContent);

    @Insert("INSERT INTO QRCodeScans (user_id, qr_content) VALUES (#{userId}, #{qrContent})")
    int insertQRCodeScan(@Param("userId") Integer userId, @Param("qrContent") String qrContent);

    @Select("SELECT COUNT(*) FROM QRCodeScans WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Integer userId);

    @Select("SELECT qr_content FROM QRCodeScans WHERE user_id = #{userId}")
    String selectQRCodeByUserId(@Param("userId") Integer userId);
}
