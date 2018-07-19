package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.dao.impl.MUnit;

/**
 * @Autor rongxiaokun
 * @Date 2018/7/13  10:12
 * 思路:check()判断整个查询类型逻辑
 *      simpleCheck()具体执行某类型查询(考虑到查询某个要素会有多个方面,比如gfs会有下载和解析两种)
 *
 */
public interface MonitorUnitService {
    /**
     *
     * @param type  数据类型
     * @return  查询总体的数据有效率
     */
    float check(String type);

    /**
     *
     * @param type  数据类型
     * @return  MUnit   封装了数据监控
     */
    MUnit simpleCheck(String type);
}
