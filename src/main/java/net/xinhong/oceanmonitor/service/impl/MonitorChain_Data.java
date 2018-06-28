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

    @Override
    public boolean isOk(String type) {
        return true;
    }

    @Override
    public String getErrInfo(String type) {
        return "error:data";
    }
}
