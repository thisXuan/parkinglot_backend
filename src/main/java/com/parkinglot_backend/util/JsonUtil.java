package com.parkinglot_backend.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.Collections;
import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-04-06
 * @Description:
 */

public class JsonUtil {
    public static String listToJson(List<String> list) {
        return JSON.toJSONString(list);
    }
}