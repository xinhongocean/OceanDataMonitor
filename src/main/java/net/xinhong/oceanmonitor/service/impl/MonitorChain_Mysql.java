package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.MonitorChain;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/6/27.
 */
@Service
public class MonitorChain_Mysql extends MonitorChain {
    public MonitorChain_Mysql() {
    }

    @Override
    public boolean isOk(String type) {
        return false;
    }

    @Override
    public String getErrInfo(String type) {
        return "error:Mysql";
    }
}
