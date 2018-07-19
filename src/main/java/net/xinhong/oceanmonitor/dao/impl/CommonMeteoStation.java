package net.xinhong.oceanmonitor.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Autor rongxiaokun
 * @Date 2018/7/18  9:49
 */
public enum  CommonMeteoStation {

//北京************************************************
    BJ("54511" , "北京"),
    CP("54499" , "昌平"),
    CY("54433" , "朝阳"),
    FT("54514" , "丰台"),
    SJS("54513" , "石景山"),
    HD("54399" , "海淀"),
    MTG("54505" , "门头沟"),
    FS("54596" , "房山"),
    TZ("58268" , "通州"),
    SY("54398" , "顺义"),
    DX("54594" , "大兴"),
    HR("54419" , "怀柔"),
    PG("54424" , "平谷"),
    MY("54416" , "密云"),
    YQ("54406" , "延庆"),
//其他城市*******************************************
    SH("58362" , "上海"),
    WH("57494" , "武汉"),
    NJ("58238" , "南京"),
    GZ("59287" , "广州"),
    SZ("59493" , "深圳"),

    ;


    private String stationNum;      //站号
    private String cName;           //中文描述

    /**
     * 私有构造,防止被外部调用
     * @param stationNum
     */
    private CommonMeteoStation(String stationNum , String cName) {
        this.stationNum = stationNum;
        this.cName = cName;
    }

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}
