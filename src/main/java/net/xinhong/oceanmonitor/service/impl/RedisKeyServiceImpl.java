package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.DecimalFormats;
import net.xinhong.oceanmonitor.common.JedisUtil;
import net.xinhong.oceanmonitor.common.PropertyUtil;
import net.xinhong.oceanmonitor.common.TimeMangerJob;
import net.xinhong.oceanmonitor.service.RedisKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/22.
 * redis的监控：针对固定的key定时查询
 *
 */
@Service
public class RedisKeyServiceImpl implements RedisKeyService , TimeMangerJob ,Serializable {
    private  final Logger logger = LoggerFactory.getLogger(RedisKeyServiceImpl.class);
    private JSONObject json = new JSONObject();
    public RedisKeyServiceImpl() {
        PropertyUtil.loadProps("monitorConf/redisKey.properties");
    }

    private JedisCluster redis = null;
    private int lossNum;                    //丢失数据
    private float dataRate;                 //数据比率
    private String date;                    //日期
    private String type;                    //数据类型（例如GFS等）
    private String result_common;
    private String result_JSYB;
    private String result_isoline;
    private String result_isosurface;

    /**
     * 功能：            数据类型查询：类型是“”时，表示遍历
     * @param type      数据类型：例如：GFS，具体查阅《Redis 缓存数据格式说明》
     * @return  float   表示数据有效率
     */
    @Override
    public float checkKeys(String type) {
        if (!json.isEmpty())json.clear();
        try {
            redis = JedisUtil.connRedis();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhh");
            //GFS:0-72小时为逐小时，72-96小时间隔3小时，VTI长度为3位,81个
            date = format.format( Calendar.getInstance().getTime());
            lossNum = 0;
            for (int i = 0; i < 73; i++) {
                simpleCheck(date ,i , type);
            }
            for (int i = 75; i < 97; i+=3) {
                simpleCheck(date ,i ,type);
            }
            dataRate = (float)(81*4-lossNum) / (81*4) * 100;
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
        return dataRate;
    }

    /**
     * @param date_in   日期：yyyyMMddhh
     * @param type      数据类型，例如：GFS
     * @return  float   表示数据有效率
     */
    //手动查询查询
    public float checkKeys(String date_in ,String type) {
        if (!json.isEmpty())json.clear();
        try {
            redis = JedisUtil.connRedis();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhh");
            date = (date_in !=null && date_in.matches("[0-9]{10}"))? date_in :
                    format.format( Calendar.getInstance().getTime());
            lossNum = 0;
            for (int i = 0; i < 73; i++) {
                simpleCheck(date ,i ,type);
            }
            for (int i = 75; i < 97; i+=3) {
                simpleCheck(date ,i , type);
            }
            dataRate = (float)(81*4-lossNum)/(81*4) * 100;
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
        return dataRate;
    }
    private void simpleCheck(String date , int i ,String type) {
        //// TODO: 2018/7/2 针对不同的数据源，用条件判断下；针对手动和自动，用type是否为空判断
        if(type.isEmpty())simpleCheck_all(date , i);                    //针对自动check，遍历all

        simpleCheck_GFS(date , i );                             //redis_gfs

    }
    //redis_gfs
    private void simpleCheck_GFS(String date , int i){
        String result_common_temp = PropertyUtil.getProperty("GFS_result_common_head") + date + DecimalFormats.decimalFormat000.format(i);
        String result_JSYB_temp = PropertyUtil.getProperty("GFS_result_JSYB_head") + date + DecimalFormats.decimalFormat000.format(i);
        String result_isoline_temp = PropertyUtil.getProperty("GFS_result_isoline_head") + date + DecimalFormats.decimalFormat000.format(i);
        String result_isosurface_temp = PropertyUtil.getProperty("GFS_result_isosurface_head") + date + DecimalFormats.decimalFormat000.format(i);
        result_common = redis.hget(result_common_temp, PropertyUtil.getProperty("GFS_result_common_point"));
        result_JSYB = redis.hget(result_JSYB_temp, PropertyUtil.getProperty("GFS_result_JSYB_point"));
        result_isoline = redis.hget(result_isoline_temp, PropertyUtil.getProperty("GFS_result_isoline_point"));
        result_isosurface = redis.hget(result_isosurface_temp, PropertyUtil.getProperty("GFS_result_isosurface_point"));
        if (result_common == null || result_common.isEmpty()) {
            lossNum++;
            json.put("GFS" + "----" + result_common_temp, 0);
        }
        if (result_JSYB == null || result_JSYB.isEmpty()) {
            lossNum++;
            json.put("GFS" + "----" + result_JSYB_temp , 0);
        }
        if (result_isoline == null || result_isoline.isEmpty()) {
            lossNum++;
            json.put("GFS" + "----" + result_isoline_temp , 0);
        }
        if (result_isosurface == null || result_isosurface.isEmpty()) {
            lossNum++;
            json.put("GFS" + "----" + result_isosurface_temp , 0);
        }
        //清空
        result_common = null;
        result_JSYB = null;
        result_isoline = null;
        result_isosurface = null;
    }
    //针对自动check，遍历all
    private void simpleCheck_all(String date , int i){
        // TODO: 2018/7/2 add方法
        simpleCheck_GFS(date , i);
    }

    public int getLossNum() {
        return lossNum;
    }

    public void setLossNum(int lossNum) {
        this.lossNum = lossNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public float getDataRate() {
        return dataRate;
    }

    public void setDataRate(float dataRate) {
        this.dataRate = dataRate;
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

    @Override
    public void doJob() {
        checkKeys("");      //针对定时执行
    }
}
