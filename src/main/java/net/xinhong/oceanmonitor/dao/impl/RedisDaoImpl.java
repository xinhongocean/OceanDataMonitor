package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.dao.RedisDAO;
import org.springframework.stereotype.Repository;

/**
 * Created by zxf on 2018/3/26.
 */
@Repository
public class RedisDaoImpl implements RedisDAO{
    @Override
    public JSONObject getInfo(String type, String lines) {
        return null;
    }
}
