package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.*;
import net.xinhong.oceanmonitor.common.tools.NumModelTimeUtils;
import net.xinhong.oceanmonitor.dao.impl.CommonMeteoStation;
import net.xinhong.oceanmonitor.dao.impl.MUnit;
import net.xinhong.oceanmonitor.service.MonitorUnitService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/22.
 * redis的监控：针对固定的key定时查询
 * 思路: check所有类型 ----simpleCheck某个类型 ----simpleCheck_type某个类型的具体实现    (扩展:增加simpleCheck_type和simpleCheck)
 */
@Service
public class RedisKeyServiceImpl implements MonitorUnitService, TimeMangerJob ,Serializable {
    private  final Logger logger = LoggerFactory.getLogger(RedisKeyServiceImpl.class);
    public RedisKeyServiceImpl() {
//        PropertyUtil.loadProps("monitorConf/redisKey.properties");
    }

    private MUnit mUnit;
    private JedisCluster redis = null;
    private int lossNum;                    //丢失数据
    private int checkNum;                   //查询总数
    private String result_common;
    private String result_area;
    private String result_JSYB;
    private String result_isoline;
    private String result_isosurface;

    /**
     * 功能：            数据类型查询：类型是“”时，表示遍历
     * @param type      数据类型：例如：GFS，具体查阅《Redis 缓存数据格式说明》
     * @return  float   表示数据有效率
     */
    @Override
    public float check(String type) {
        float dataRate = simpleCheck(type).getDataRate();
        return dataRate;
    }

    /**
     *
     * @param type  数据类型    目前4中数据类型:GFS,ZDCSYBZL,HY1SUO,WAVEWATCH
     * @return
     */
    @Override
    public MUnit simpleCheck(String type) {
        mUnit = new MUnit();
        try {
            if (type.contains("HY1SUO")){
                redis = JedisUtil.connRedis("21");
            }else {
                redis = JedisUtil.connRedis("114");
            }
            //GFS:0-72小时为逐小时，72-96小时间隔3小时，VTI长度为3位,81个
            String date = NumModelTimeUtils.getGFSDateTime(new DateTime() ,0);  //todo 只检测当前时间对应的一个起报
            lossNum = 0;
            checkNum = 0;
            /*********************
             * GFS
             *********************/
            if(type.contains("GFS")) {
                for (int i = 0; i < 73; i++) {
                    simpleCheckBridge(date, i, type, mUnit.getJson());
                }
                for (int i = 75; i < 97; i += 3) {
                    simpleCheckBridge(date, i, type, mUnit.getJson());
                }
                checkNum = 81 *2 +2;
                float dataRate = (float) (checkNum - lossNum) / checkNum * 100;
                mUnit.setDataRate(dataRate);
            }
            /*********************
             * 城镇精细化预报
             *********************/
            if (type.contains("ZDCSYBZL")){
                simpleCheck_ZDCSYBZL(date ,mUnit.getJson());
                float dataRate = (float) (checkNum - lossNum) / checkNum * 100;
                mUnit.setDataRate(dataRate);
            }
            /*********************
             * HY1SUO
             *********************/
            if (type.contains("HY1SUO")){
                simpleCheck_HY1SUO(date ,mUnit.getJson());
                float dataRate = (float) (checkNum - lossNum) / checkNum * 100;
                mUnit.setDataRate(dataRate);
            }
            /*********************
             * WAVEWATCH
             *********************/
            if(type.contains("WAVEWATCH")){
                simpleCheck_WAVEWATCH(date ,mUnit.getJson());
                float dataRate = (float) (checkNum - lossNum) / checkNum * 100;
                mUnit.setDataRate(dataRate);
            }
        }catch (Exception e){
            logger.error(e.toString());
        }finally {
            if (redis != null) {
                try {
                    redis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mUnit;
    }

    private void simpleCheckBridge(String date , int i ,String type ,JSONObject json) {
        //// TODO: 2018/7/2 针对不同的数据源，用条件判断下；针对手动和自动，用type是否为空判断
        if(type.isEmpty() || type == null) {
            simpleCheck_all(date, i ,json);                    //针对自动check，遍历all
        }else {
            simpleCheck_GFS(date , i ,json);                             //redis_gfs
        }
    }
    /*********************
     * GFS    两种数据带vti,两种不带
     *********************/
    private void simpleCheck_GFS(String date , int i , JSONObject json){
        String result_common_temp = PropertyUtil.getProperty("GFS_result_common_head") + date + DecimalFormats.decimalFormat000.format(i);
        String result_JSYB_temp = PropertyUtil.getProperty("GFS_result_JSYB_head") + date + DecimalFormats.decimalFormat000.format(i);

        result_common = redis.hget(result_common_temp, PropertyUtil.getProperty("GFS_result_common_point"));
        result_JSYB = redis.hget(result_JSYB_temp, PropertyUtil.getProperty("GFS_result_JSYB_point"));

        if (result_common == null || result_common.isEmpty()) {
            lossNum++;
            json.put("GFS" + "----" + result_common_temp, 0);
        }
        if (result_JSYB == null || result_JSYB.isEmpty()) {
            lossNum++;
            json.put("GFS" + "----" + result_JSYB_temp , 0);
        }
        if(i == 0){
            String result_isoline_temp = PropertyUtil.getProperty("GFS_result_isoline_head") + date ;
            String result_isosurface_temp = PropertyUtil.getProperty("GFS_result_isosurface_head") + date ;
            result_isoline = redis.hget(result_isoline_temp, PropertyUtil.getProperty("GFS_result_isoline_point"));
            result_isosurface = redis.hget(result_isosurface_temp, PropertyUtil.getProperty("GFS_result_isosurface_point"));
            if ( result_isoline == null || result_isoline.isEmpty() ) {
                lossNum++;
                json.put("GFS" + "----" + result_isoline_temp , 0);
            }
            if ( result_isosurface == null || result_isosurface.isEmpty()  ) {
                lossNum++;
                json.put("GFS" + "----" + result_isosurface_temp , 0);
            }
            result_isoline = null;
            result_isosurface = null;
        }

        //清空
        result_common = null;
        result_JSYB = null;
    }
    /*********************
     * 城镇精细化预报
     *********************/
    private void simpleCheck_ZDCSYBZL(String date ,JSONObject json){
        List<String> stations = new ArrayList<>();                                                                  //站名
        for (CommonMeteoStation ele: CommonMeteoStation.values()) {
            stations.add(ele.getStationNum());
        }
        List<String> elements = new ArrayList<>();                                                                  //要素名
        for (ElemCityFC ele: ElemCityFC.values()) {
            elements.add(ele.name());
        }
        String result_common_temp = PropertyUtil.getProperty("CZJXHYB_result_common_head")+date.substring(0,8);

        for (int i = 0; i < stations.size(); i++) {
            for (int j = 0; j < elements.size(); j++) {
                result_common = redis.hget(result_common_temp,stations.get(i)+"_"+date+elements.get(j));
                if (result_common == null || result_common.isEmpty()) {
                    lossNum++;
                    json.put("城镇精细化预报" + "----" + result_common_temp+stations.get(i)+"_"+date+elements.get(j), 0);
                }
                result_common = null;
            }
        }
        for (int i = 0; i < stations.size(); i++) {
            result_common = redis.hget(result_common_temp,stations.get(i)+"_"+date);
            if (result_common == null || result_common.isEmpty()) {
                lossNum++;
                json.put("城镇精细化预报" + "----" + result_common_temp+stations.get(i)+"_"+date, 0);
            }
            result_common = null;
        }

        checkNum = stations.size() * elements.size() +stations.size();
    }

    /*********************
     * HY1SUO
     *********************/
    private void simpleCheck_HY1SUO(String date , JSONObject json){
        if (date.substring(8,10).equals("00"))date = NumModelTimeUtils.getGFSDateTime(new DateTime() ,10);      //只有12时起报
        String result_common_temp = PropertyUtil.getProperty("HY1SUO_result_point_head")+date;
        String result_area_temp = PropertyUtil.getProperty("HY1SUO_result_area_head")+date;
        String result_isosurface_temp = PropertyUtil.getProperty("HY1SUO_result_isosurface_head");
        String[] areaEle = PropertyUtil.getProperty("HY1SUO_result_area_type").split(",");
        String[] level = PropertyUtil.getProperty("HY1SUO_result_isosurface_num").split(",");
        String[] isosurfaceEle = PropertyUtil.getProperty("HY1SUO_result_isosurface_type").split(",");
        for (int i = 0; i < 145; i+=3) {
            //HY1SUO_result_area_head
            for (int j = 0; j < areaEle.length; j++) {
                result_area = redis.hget(result_area_temp+"_"+DecimalFormats.decimalFormat000.format(i) , areaEle[j]);
                if (result_area == null || result_area.isEmpty()) {
                    lossNum++;
                    json.put("HY1SUO" + "----" + result_area_temp+DecimalFormats.decimalFormat000.format(i), 0);
                }
                result_area = null;
            }
            //HY1SUO_result_point_head
            result_common = redis.hget(result_common_temp+DecimalFormats.decimalFormat000.format(i) , PropertyUtil.getProperty("HY1SUO_result_point_point"));
            if (result_common == null || result_common.isEmpty()) {
                lossNum++;
                json.put("HY1SUO" + "----" + result_common_temp+DecimalFormats.decimalFormat000.format(i), 0);
            }
            result_common = null;
            //HY1SUO_result_isosurface
            for (int j = 0; j < level.length; j++) {
                for (int k = 0; k < isosurfaceEle.length; k++) {
                    result_isosurface = redis.hget(result_isosurface_temp + date + DecimalFormats.decimalFormat000.format(i) ,
                            level[j] + "_" + isosurfaceEle[k] + "_EN");
                    if (result_isosurface == null || result_isosurface.isEmpty()) {
                        lossNum++;
                        json.put("HY1SUO" + "----" + result_isosurface_temp + date + DecimalFormats.decimalFormat000.format(i), 0);
                    }
                    result_isosurface = null;
                }
            }
        }

        checkNum = 49 * (1 + areaEle.length + level.length * isosurfaceEle.length);

    }
    /*********************
     * WAVEWATCH
     *********************/
    private void simpleCheck_WAVEWATCH(String date , JSONObject json){
        date = date.substring(0,8);
        String result_area_temp = PropertyUtil.getProperty("WAVEWATCH_result_area_head")+date+"_";
        String result_data_temp = PropertyUtil.getProperty("WAVEWATCH_result_data_head")+date+"_";
        String result_image_temp = PropertyUtil.getProperty("WAVEWATCH_result_image_head"+"_");
        String[] areaEle = PropertyUtil.getProperty("WAVEWATCH_result_area_type").split(",");
        String[] imageType = PropertyUtil.getProperty("WAVEWATCH_result_image_type").split(",");
        //area
        for (int i = 0; i < 81; i++) {
            for (int j = 0; j < areaEle.length; j++) {
                result_area = redis.hget(result_area_temp+"_"+DecimalFormats.decimalFormat000.format(i) , areaEle[j]);
                if (result_area == null || result_area.isEmpty()) {
                    lossNum++;
                    json.put("WAVEWATCH" + "----" + result_area_temp+DecimalFormats.decimalFormat000.format(i), 0);
                }
                result_area = null;
            }
        }
        for (int i = 0; i < 241; i+=3) {
            //data
            result_common = redis.hget(result_data_temp+"_"+DecimalFormats.decimalFormat000.format(i) , PropertyUtil.getProperty("WAVEWATCH_result_area_point"));
            if (result_common == null || result_common.isEmpty()) {
                lossNum++;
                json.put("WAVEWATCH" + "----" + result_data_temp+DecimalFormats.decimalFormat000.format(i), 0);
            }
            result_common = null;
            //image
            for (int j = 0; j < imageType.length; j++) {
                result_common = redis.hget(result_image_temp+"_"+DecimalFormats.decimalFormat000.format(i) , PropertyUtil.getProperty("WAVEWATCH_result_area_point"));
                if (result_common == null || result_common.isEmpty()) {
                    lossNum++;
                    json.put("WAVEWATCH" + "----" + result_image_temp+DecimalFormats.decimalFormat000.format(i), 0);
                }
                result_common = null;
            }
        }

        checkNum = 81 * (areaEle.length + 1 + imageType.length);
    }
    //针对自动check，遍历all
    private void simpleCheck_all(String date , int i ,JSONObject json){
        // TODO: 2018/7/2 add方法
        simpleCheck_GFS(date , i ,json);
    }

    public int getLossNum() {
        return lossNum;
    }

    public void setLossNum(int lossNum) {
        this.lossNum = lossNum;
    }

    public Logger getLogger() {
        return logger;
    }

    public JedisCluster getRedis() {
        return redis;
    }

    public void setRedis(JedisCluster redis) {
        this.redis = redis;
    }

    public String getResult_common() {
        return result_common;
    }

    public void setResult_common(String result_common) {
        this.result_common = result_common;
    }

    public String getResult_JSYB() {
        return result_JSYB;
    }

    public void setResult_JSYB(String result_JSYB) {
        this.result_JSYB = result_JSYB;
    }

    public String getResult_isoline() {
        return result_isoline;
    }

    public void setResult_isoline(String result_isoline) {
        this.result_isoline = result_isoline;
    }

    public String getResult_isosurface() {
        return result_isosurface;
    }

    public void setResult_isosurface(String result_isosurface) {
        this.result_isosurface = result_isosurface;
    }

    public MUnit getmUnit() {
        return mUnit;
    }

    public void setmUnit(MUnit mUnit) {
        this.mUnit = mUnit;
    }

    @Override
    public void doJob() {
        check("");      //针对定时执行
    }


}
