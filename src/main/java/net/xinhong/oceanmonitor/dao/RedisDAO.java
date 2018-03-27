package net.xinhong.oceanmonitor.dao;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zxf on 2018/3/26.
 */
public interface RedisDAO {
    JSONObject getInfo(String type, String lines);
}
