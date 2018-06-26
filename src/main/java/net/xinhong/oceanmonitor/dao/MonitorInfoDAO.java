package net.xinhong.oceanmonitor.dao;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2018/6/21.
 */
public interface MonitorInfoDAO {
    JSONObject getInfo(String source);
}
