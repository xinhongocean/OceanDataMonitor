package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.MonitorChain;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Mysql extends MonitorChain implements Serializable {
    public MonitorChain_Mysql() {
    }

//    @Autowired
    private MysqlDataServiceImpl mysqlDataServiceImpl = new MysqlDataServiceImpl();
    @Override
    public boolean isOk(String type) {
        float dataRate = mysqlDataServiceImpl.check(type);
        System.out.println("mysql "+dataRate);
        return (dataRate>99)?true:false;
    }

    @Override
    public String getErrInfo(String type) {
        return "Type:"+type+"  Model:MonitorChain_Mysql"+"  Body:"+mysqlDataServiceImpl.getmUnit().getJson();
    }
}
