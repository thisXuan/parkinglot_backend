package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.entity.Car;
import com.parkinglot_backend.service.CarService;
import com.parkinglot_backend.mapper.CarMapper;
import org.springframework.stereotype.Service;

/**
* @author minxuan
* @description 针对表【Car】的数据库操作Service实现
* @createDate 2024-12-26 11:03:49
*/
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car>
    implements CarService{

}




