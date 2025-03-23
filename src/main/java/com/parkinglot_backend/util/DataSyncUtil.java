package com.parkinglot_backend.util;

import org.elasticsearch.action.index.IndexRequest;
import com.alibaba.fastjson.JSON;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.mapper.StoreMapper;
import jakarta.annotation.Resource;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-06
 * @Description:
 */
@Component
public class DataSyncUtil {
    @Resource
    private RestHighLevelClient esClient; // Elasticsearch 客户端

    @Resource
    private StoreMapper storeMapper; // 数据库操作类（MyBatis Mapper）

//    @Scheduled(fixedRate = 360000) // 每小时同步一次
//    public void syncData() {
//        List<Store> stores = storeMapper.findAllStores(); // 从数据库获取所有店铺数据
//        for (Store store : stores) {
//            // 使用构建器模式创建 Elasticsearch 索引请求
//            IndexRequest request = new IndexRequest("store_index")  // 索引名称
//                    .id(store.getId().toString())  // 文档 ID
//                    .source(JSON.toJSONString(store), XContentType.JSON);  // 文档内容
//
//            try {
//                esClient.index(request, RequestOptions.DEFAULT);  // 执行索引操作
//                //System.out.println("Indexed store: " + store.getId());  // 添加日志记录
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

