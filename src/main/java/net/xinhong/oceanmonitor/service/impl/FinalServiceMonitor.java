package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.service.MonitorChain;


//处理最终接口数据
public class FinalServiceMonitor extends MonitorChain {
    @Override
    public boolean isOk(String type) {
        return false;
    }

    @Override
    public String getErrInfo(String type) {
        return null;
    }


}
