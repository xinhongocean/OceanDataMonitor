package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wingsby on 2018/3/19.
 */
public interface HardWareService {
    JSONObject getInfo(String time,String machine);
}
