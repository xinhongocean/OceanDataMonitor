package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.MonitorChain;
import net.xinhong.oceanmonitor.service.RedisKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Redis extends MonitorChain {
    public MonitorChain_Redis() {
    }

    @Autowired
    RedisKeyService redisKeyService;
    @Override
    public boolean isOk(String type) {
        return  false;
//        float dataRate =redisKeyService.checkKeys();    //自动查询
//        return (dataRate>0.95)? true:false;
    }

    @Override
    public String getErrInfo(String type) {
        return "error:redis";
    }
}
