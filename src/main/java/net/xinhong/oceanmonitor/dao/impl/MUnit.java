package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Autor rongxiaokun
 * @Date 2018/7/13  17:16
 */
public class MUnit {
    private static final Logger LOGGER = LoggerFactory.getLogger(MUnit.class);

    private float dataRate = 0;     //监控数据率
    private int dataNum = 0;        //理论数据个数
    private int dataNum_real = 0;   //实际数据个数
    private JSONObject json = new JSONObject();    //无效数据keys

    public MUnit() {
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public float getDataRate() {
        return dataRate;
    }

    public void setDataRate(float dataRate) {
        this.dataRate = dataRate;
    }

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public int getDataNum_real() {
        return dataNum_real;
    }

    public void setDataNum_real(int dataNum_real) {
        this.dataNum_real = dataNum_real;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
