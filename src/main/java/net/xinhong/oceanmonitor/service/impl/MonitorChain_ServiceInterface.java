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
    @Autowired
    ServiceInterfaceImpl serviceInterface;
    @Override
    public boolean isOk(String type) {
        ServiceInterfaceImpl serviceInterface = new ServiceInterfaceImpl();
        serviceInterface.check(type);
        return false;       ///////////////////////////////
//        return serviceInterface.getJson().isEmpty();
    }

    @Override
    public String getErrInfo(String type) {
//        return "Type:"+type+"  Model:ServiceInterface"+"  Body:"+serviceInterface.getJson();
        return "Type:"+type+"  Model:ServiceInterface"+"  Body:";
    }
}
