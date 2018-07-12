package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.DataAdress;
import net.xinhong.oceanmonitor.service.MonitorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Data extends MonitorChain {
    public MonitorChain_Data() {
    }

    DataAdressImpl dataAdress = new DataAdressImpl() ;
    @Override
    public boolean isOk(String type) {
        Map<String ,Float> map = dataAdress.checks(type);
        for (String dataAdressType :map.keySet()) {
            float dataRate = map.get(dataAdressType);
            if (dataRate<=70){
                System.out.println("Data  "+false);
                return false;
            }
        }
        System.out.println("Data  "+true);
        return true;
    }

    @Override
    public String getErrInfo(String type) {
//        return "error:data";
        return "Type:"+type+"  Model:DataAdressImpl"+"  Body:"+dataAdress.getJson();
    }
}
