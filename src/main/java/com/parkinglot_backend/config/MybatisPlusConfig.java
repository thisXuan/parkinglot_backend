package com.parkinglot_backend.config;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Configuration;

//import javax.annotation.PostConstruct;
import java.util.List;

//@Configuration
public class MybatisPlusConfig {

//    private final List<SqlSessionFactory> sqlSessionFactoryList;
//
//    public MybatisPlusConfig(List<SqlSessionFactory> sqlSessionFactoryList) {
//        this.sqlSessionFactoryList = sqlSessionFactoryList;
//    }
//
//    @PostConstruct
//    public void registerTypeHandlers() {
//        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
//            TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
//            // 注册Fastjson处理器
//            typeHandlerRegistry.register(FastjsonTypeHandler.class);
//        }
//    }
}