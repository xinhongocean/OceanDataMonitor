package net.xinhong.oceanmonitor.dao;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wingsby on 2018/3/20.
 */
public interface ExcutorOperationDao {
    JSONObject start(String type);

    JSONObject stop(String type);

    JSONObject status(String type);

    JSONObject query();
}
