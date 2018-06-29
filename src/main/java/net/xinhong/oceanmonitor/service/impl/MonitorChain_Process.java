package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.service.MonitorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Process implements Serializable {
    private MonitorChain_Process() {}
    private static MonitorChain_Process instance;
    public static MonitorChain_Process getInstance() {
        if (instance == null) {
            synchronized (MonitorChain_Process.class) {
                if (instance == null) {
                    instance = new MonitorChain_Process();
                }
            }
        }
        return instance;
    }

    private MonitorChain monitorChain;

    public JSONObject deal(String type){
         JSONObject json = monitorChain.handleMonitor(type , new JSONObject());
         return json;
     }
    public void setMonitorChain(MonitorChain monitorChain){
        this.monitorChain = monitorChain;
    }

    public MonitorChain getMonitorChain() {
        return monitorChain;
    }

}
