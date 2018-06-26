package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2018/6/21.
 */
public interface MonitorInfoService {
    JSONObject getInfo(String source);

    JSONObject getDownloadInfo(String source);

    JSONObject getParseInfo(String source);


}
