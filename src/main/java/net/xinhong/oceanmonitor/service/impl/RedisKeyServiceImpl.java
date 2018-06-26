package net.xinhong.oceanmonitor.service.impl;

import net.xinhong.oceanmonitor.common.DecimalFormats;
import net.xinhong.oceanmonitor.common.JedisUtil;
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
 */
@Service
public class RedisKeyServiceImpl implements RedisKeyService , TimeMangerJob ,Serializable {
    private  final Logger logger = LoggerFactory.getLogger(RedisKeyServiceImpl.class);

    public RedisKeyServiceImpl() {
    }

    private JedisCluster redis = null;
    private int lossNum;
    private float dataRate;
    private String date;
    private String result_common;
    private String result_JSYB;
    private String result_isoline;
    private String result_isosurface;
    @Override
    public float checkKeys() {
        // TODO: 2018/6/22 加入所有的key
        try {
            redis = JedisUtil.connRedis();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhh");
            //GFS:0-72小时为逐小时，72-96小时间隔3小时，VTI长度为3位,81个
            date = format.format( Calendar.getInstance().getTime());
            lossNum = 0;
            for (int i = 0; i < 73; i++) {
                simpleCheck(date ,i);
            }
            for (int i = 75; i < 97; i+=3) {
                simpleCheck(date ,i);
            }
            dataRate = (float)(81*4-lossNum)/(81*4);
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
    //手动查询查询
    public float checkKeys(String date_in) {
        // TODO: 2018/6/22 加入所有的key
        try {
            redis = JedisUtil.connRedis();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhh");
            date = (date_in !=null && date_in.matches("[0-9]{10}"))? date_in :
                    format.format( Calendar.getInstance().getTime());
            lossNum = 0;
            for (int i = 0; i < 73; i++) {
                simpleCheck(date ,i);
            }
            for (int i = 75; i < 97; i+=3) {
                simpleCheck(date ,i);
            }
            dataRate = (float)(81*4-lossNum)/(81*4);
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
    private void simpleCheck(String date , int i){
        result_common = redis.hget("xhgfs:fc:point:"+date+DecimalFormats.decimalFormat000.format(i) , "82.00_99.00");
        result_JSYB = redis.hget("xhgfs:fc:point:sigmet:"+date+DecimalFormats.decimalFormat000.format(i) , "43.50_-91.50");
        result_isoline = redis.hget("xhgfs:fc:isoline:"+date+DecimalFormats.decimalFormat000.format(i) , "005_TT_0925_EN");
        result_isosurface = redis.hget("xhgfs:fc:isosurface:"+date+DecimalFormats.decimalFormat000.format(i) , "049_TURB_0850_EN");
        if (result_common == null || result_common.isEmpty())lossNum++;
        if (result_JSYB == null || result_JSYB.isEmpty())lossNum++;
        if (result_isoline == null || result_isoline.isEmpty())lossNum++;
        if (result_isosurface == null || result_isosurface.isEmpty())lossNum++;
        //清空
        result_common = null;
        result_JSYB = null;
        result_isoline = null;
        result_isosurface = null;
    }

    @Override
    public void doJob() {
        checkKeys();
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
}
