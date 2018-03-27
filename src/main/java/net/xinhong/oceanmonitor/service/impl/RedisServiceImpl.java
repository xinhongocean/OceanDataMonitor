package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.dao.RedisDAO;
import net.xinhong.oceanmonitor.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zxf on 2018/3/26.
 */
@Service
public class RedisServiceImpl implements RedisService{
    @Autowired
    private RedisDAO dao;

    @Override
    public JSONObject getInfo(String portID) {
        return null;
    }
}
