package com.parkinglot_backend;

import com.parkinglot_backend.dto.StoreDTO;
import com.parkinglot_backend.mapper.ShopLocationMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class ParkinglotBackendApplicationTests {
    @Resource
    private ShopLocationMapper shopLocationMapper;

    @Test
    @Transactional
    void testSelectAllShopLocations() {
        // 调用 Mapper 方法
        List<StoreDTO> shopLocations = shopLocationMapper.selectAllShopLocations();

        // 验证结果不为空
        //assertThat(shopLocations).isNotNull();

        // 验证返回的列表至少包含一条记录
        //assertThat(shopLocations).isNotEmpty();

        // 如果您知道数据库中应该返回的具体记录数，可以进行更具体的验证
        // assertThat(shopLocations.size()).isEqualTo(预期的记录数);

        // 您可以进一步验证返回对象的内容，例如：
        if (!shopLocations.isEmpty()) {
            StoreDTO firstLocation = shopLocations.get(0);
            //assertThat(firstLocation.getName()).isNotNull();
            //assertThat(firstLocation.getX()).isNotNull();
            // 等等，根据 ShopLocationDTO 类中定义的字段进行验证
        }
    }
}
