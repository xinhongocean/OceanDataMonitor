package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wingsby on 2018/3/19.
 */
public interface LogInfoService {
    JSONObject getInfo(String type,String lines,String key);

    JSONObject getDownInfo(String type,String year,String month,
                           String day);

    JSONObject firstQuery(String str);
}
