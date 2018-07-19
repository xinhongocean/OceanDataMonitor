package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.MonitorChain;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Data extends MonitorChain {
    public MonitorChain_Data() {
    }

    DataAdressServiceImpl dataAdressService = new DataAdressServiceImpl() ;
    @Override
    public boolean isOk(String type) {
        float dataRate = dataAdressService.check(type);
        System.out.println("mysql "+dataRate);
        return (dataRate>99)?true:false;
//        System.out.println("data  false");
//        return false;
    }

    @Override
    public String getErrInfo(String type) {
//        return "error:data";
        return "Type:"+type+"  Model:DataAdressImpl"+"  Body:"+dataAdressService.getmUnit().getJson();
    }
}
