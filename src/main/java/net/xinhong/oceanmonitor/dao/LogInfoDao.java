package net.xinhong.oceanmonitor.dao;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wingsby on 2018/3/20.
 */
public interface LogInfoDao {
    JSONObject getInfo(String type, String lines,String key);

    JSONObject getDownInfo(String type,String year,String month,
                           String day);

    JSONObject firstQuery(String str);
}
