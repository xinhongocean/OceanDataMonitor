package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.MonitorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_ServiceInterface extends MonitorChain {
    public MonitorChain_ServiceInterface() {
    }
//    @Autowired
//    ServiceInterfaceServiceImpl serviceInterface ;
    ServiceInterfaceServiceImpl serviceInterface = new ServiceInterfaceServiceImpl();
    @Override
    public boolean isOk(String type) {
//        serviceInterface = new ServiceInterfaceServiceImpl();
        serviceInterface.check(type);
        return serviceInterface.getmUnit().getJson().isEmpty();
//        System.out.println("ServiceInterface"+false);
//        return false;
    }

    @Override
    public String getErrInfo(String type) {
        return "Type:"+type+"  Model:ServiceInterface"+"  Body:"+serviceInterface.getmUnit().getJson();
    }
}
