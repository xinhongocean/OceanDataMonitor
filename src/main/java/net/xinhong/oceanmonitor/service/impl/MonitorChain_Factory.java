package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.service.MonitorChain;
import org.apache.ibatis.javassist.expr.Instanceof;
import org.springframework.stereotype.Service;
import sun.security.jca.GetInstance;

/**
 * 作用：创建责任链上各对象
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Factory {

    private static MonitorChain serviceInterface;
    private static MonitorChain redis;
    private static MonitorChain data;
    private static MonitorChain mysql;
    private MonitorChain_Factory(){

    }
    public static MonitorChain createMonitorChainElement() {
        if (!(serviceInterface instanceof MonitorChain_ServiceInterface
                && redis instanceof MonitorChain_Redis
                &&data instanceof MonitorChain_Data
                &&mysql instanceof MonitorChain_Mysql )) {
            // TODO Auto-generated method stub
            serviceInterface = new MonitorChain_ServiceInterface();
            redis = new MonitorChain_Redis();
            data = new MonitorChain_Data();
            mysql = new MonitorChain_Mysql();

            serviceInterface.setNext(redis);
            redis.setNext(data);
            data.setNext(mysql);
        }

        return serviceInterface;
    }

    public static MonitorChain getServiceInterface() {
        return serviceInterface;
    }

    public static void setServiceInterface(MonitorChain serviceInterface) {
        MonitorChain_Factory.serviceInterface = serviceInterface;
    }

    public static MonitorChain getRedis() {
        return redis;
    }

    public static void setRedis(MonitorChain redis) {
        MonitorChain_Factory.redis = redis;
    }

    public static MonitorChain getData() {
        return data;
    }

    public static void setData(MonitorChain data) {
        MonitorChain_Factory.data = data;
    }

    public static MonitorChain getMysql() {
        return mysql;
    }

    public static void setMysql(MonitorChain mysql) {
        MonitorChain_Factory.mysql = mysql;
    }
}
