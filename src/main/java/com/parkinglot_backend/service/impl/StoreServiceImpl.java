package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.parkinglot_backend.entity.CollectTable;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.entity.Voucher;
import com.parkinglot_backend.mapper.CollectTableMapper;
import com.parkinglot_backend.mapper.ParkingSpotMapper;
import com.parkinglot_backend.service.StoreService;
import com.parkinglot_backend.mapper.StoreMapper;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
* @author minxuan
* @description 针对表【Store】的数据库操作Service实现
* @createDate 2024-10-27 11:16:24
*/
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{

    @Autowired
    private RestHighLevelClient esClient;
    @Resource
    private StoreMapper storeMapper;

    @Resource
    private ParkingSpotMapper parkingSpotMapper;

    @Resource
    private CollectTableMapper collectTableMapper;

    @Override
    public Result getStoreInfo(int page) {
        // 设置分页, 固定设置每页查询为10页
        PageHelper.startPage(page,10);
        List<Store> list = query().list();
        PageInfo<Store> pageInfo = new PageInfo<>(list);
        return Result.ok(pageInfo.getList());
    }

    @Override
    public Result queryStoreInfo(String query) {
        SearchRequest searchRequest = new SearchRequest("store_index"); // 索引名称
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query, "storeName", "address");
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "storeName", "address")); // 根据店铺名称和地址搜索

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
            List<Store> stores = parseSearchResponse(searchResponse);
            if (stores.isEmpty()) {
                return Result.fail("未查找到相关店铺");
            }
            return Result.ok(stores);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("查询失败");
        }
    }

    public List<Store> parseSearchResponse(SearchResponse searchResponse) throws IOException {
        List<Store> stores = new ArrayList<>();
        SearchHits hits = searchResponse.getHits(); // 获取搜索命中的文档列表
        SearchHit[] hitArray = hits.getHits(); // 将命中的文档列表转换为数组

        for (SearchHit hit : hitArray) {
            Store store = new Store(); // 创建一个新的 Store 对象
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 获取文档的原始 JSON 内容

            // 根据 Store 类的字段提取数据
            store.setId((Integer) sourceAsMap.get("id"));
            store.setStoreName((String) sourceAsMap.get("storeName"));
            store.setServiceCategory((String) sourceAsMap.get("serviceCategory"));
            store.setServiceType((String) sourceAsMap.get("serviceType"));
            store.setBusinessHours((String) sourceAsMap.get("businessHours"));
            store.setAddress((String) sourceAsMap.get("address"));
            store.setFloorNumber((Integer) sourceAsMap.get("floorNumber"));
            store.setDescription((String) sourceAsMap.get("description"));
            store.setRecommendedServices((Object) sourceAsMap.get("recommendedServices"));
            store.setImage((String) sourceAsMap.get("image"));

            stores.add(store); // 将 Store 对象添加到列表中
        }
        return stores; // 返回包含所有 Store 对象的列表
    }

    @Override
    public Result getServiceCategory(String query) {
        List<Store> stores;
        if ("全部".equals(query)) {
            stores = storeMapper.findAllStores(); // 这个方法返回所有商店
        } else {
            stores = storeMapper.findStoresByCategory(query); // 根据类别查询商店
        }
        if (stores.isEmpty()) {
            return Result.fail("未查找到相关店铺");
        }
        return Result.ok(stores);
    }

    @Override
    public Result getStoreName(String query) {
//        List<String> allNames = storeMapper.findAllStoreNames();
//        List<String> parkingNames = parkingSpotMapper.findAllName();
//        allNames.addAll(parkingNames);
//        return Result.ok(allNames);

        SearchRequest searchRequest = new SearchRequest("store_index"); // 索引名称
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "storeName", "address")); // 根据店铺名称和地址搜索
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
            List<String> storeNames = parseStoreNames(searchResponse); // 修改为提取 storeName 列表

            // 使用 MyBatis-Plus 进行模糊查询
            List<String> parkingNames = parkingSpotMapper.findSpotNamesByQuery(query);

            // 合并两个结果列表，并去重
            List<String> allNames = new ArrayList<>(storeNames);
            allNames.addAll(parkingNames);
            allNames = new ArrayList<>(new LinkedHashSet<>(allNames)); // 去重

            if (allNames.isEmpty()) {
                return Result.fail("未查找到相关店铺");
            }
            return Result.ok(allNames); // 返回合并后的 storeName 列表
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("查询失败");
        }
    }

    private List<String> parseStoreNames(SearchResponse searchResponse) {
        List<String> storeNames = new ArrayList<>();
        // 遍历搜索结果
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            // 将每个搜索结果的 JSON 字符串解析为 Map
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            // 提取 storeName 字段
            Object storeName = sourceMap.get("storeName");
            if (storeName != null) {
                storeNames.add(storeName.toString());
            }
        }
        return storeNames;
    }

    @Override
    public Result getStoresByFilters(String category, String floor, int page, int size) {
        // 对 floor 参数进行转换
        if ("B1".equals(floor)) {
            floor = "-1";
        } else if ("M".equals(floor)) {
            floor = "0";
        }

        // 自动处理分页，size固定为10
        PageHelper.startPage(page, 10);
        //System.out.println(floor);

        // 调用 StoreMapper 获取筛选后的商铺列表
        List<Store> stores = storeMapper.getStoresByFilters(category, floor);

        // 返回结果
        return Result.ok(stores);
    }

    @Override
    public Result getStoreInfoById(int id) {
        Store store = storeMapper.selectById(id);
        if(store == null){
            return Result.fail("该店铺不存在！");
        }
        return Result.ok(store);
    }

    @Override
    public Result addFavoriteStore(String token, int storeId) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        if (userId == null) {
            return Result.fail("未登录");
        }

        // 检查记录是否已存在
        boolean exists = collectTableMapper.selectExistUserStore(userId, storeId) > 0;
        if (exists) {
            return Result.fail("已经收藏过该店铺");
        }

        // 插入新记录
        int rowsAffected = collectTableMapper.insertUserStore(userId, storeId);
        if (rowsAffected == 0) {
            return Result.fail("收藏失败");
        } else {
            return Result.ok("收藏成功");

        }
    }

    @Override
    public Result removefavoriteStore(String token, int storeId) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        if (userId == null) {
            return Result.fail("未登录");
        }

        // 删除记录
        int rowsAffected = collectTableMapper.deleteFavoriteStore(userId, storeId);
        if (rowsAffected == 0) {
            return Result.fail("未收藏该店铺");
        } else {
            return Result.ok("取消收藏成功");
        }
    }

    @Override
    public Result viewfavoritesStore(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        if (userId == null) {
            return Result.fail("未登录");
        }

        // 查询用户的收藏店铺信息
        List<Store> favoriteStores = collectTableMapper.selectAllFavoriteStores(userId);

        if (favoriteStores.isEmpty()) {
            return Result.ok("没有收藏的店铺");
        } else {
            return Result.ok(favoriteStores);
        }

    }

    @Override
    public Result viewLikesByStore(String token, int storeId) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        QueryWrapper<CollectTable> storeId1 = new QueryWrapper<CollectTable>().eq("store_id", storeId).eq("user_id",userId);
        CollectTable collectTable = collectTableMapper.selectOne(storeId1);
        if (collectTable == null) {
            return Result.ok(0);
        }
        return Result.ok(1);
    }


}




