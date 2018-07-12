package net.xinhong.oceanmonitor.service;

import java.util.Map;

/**
 * Created by Administrator on 2018/7/3.
 */
public interface DataAdress {
    float check(String type);
    Map<String , Float> checks(String type);
}
