package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.MonitorChain;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Redis extends MonitorChain {
    public MonitorChain_Redis() {
    }

//    @Autowired
    RedisKeyServiceImpl redisKeyService = new RedisKeyServiceImpl();
    @Override
    public boolean isOk(String type) {
//        float dataRate =redisKeyService.check(type);    //自动查询
//        System.out.println("redis"+(dataRate));
//        return (dataRate>99)? true:false;
        System.out.println("redis  " +false);
        return true;
    }

    @Override
    public String getErrInfo(String type) {
//        return "error:redis";
        return "Type:"+type+"  Model:RedisKeyService"+"  Body:"+redisKeyService.getmUnit().getJson();
    }
}
