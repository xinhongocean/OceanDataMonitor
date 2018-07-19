package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.*;
import net.xinhong.oceanmonitor.common.DataCheckUtil;
import net.xinhong.oceanmonitor.common.PropertyUtil;
import net.xinhong.oceanmonitor.common.ftp.SFTPUtil;
import net.xinhong.oceanmonitor.common.tools.NumModelTimeUtils;
import net.xinhong.oceanmonitor.dao.impl.MUnit;
import net.xinhong.oceanmonitor.service.MonitorUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * 功能：
 * type：GFS_download、GFS_parse、
 * 注意：由于数据下载速度不定，下载进度可能超过 100%
 *      JSYB的数据比较多,使用DB数据代替查询整个
 * Created by Administrator on 2018/7/3.
 */
@Service
public class DataAdressServiceImpl implements MonitorUnitService,Serializable{
    private  final Logger logger = LoggerFactory.getLogger(RedisKeyServiceImpl.class);
    private MUnit mUnit;
    private String date = NumModelTimeUtils.getGFSDateTime(NumModelTimeUtils.getDateTime() , 0);
    @Override
    public float check(String type) {
        float dataRate = 0;
        if (type.equals("GFS")){
            MUnit mUnit_download = simpleCheck(type+"_download");
            MUnit mUnit_parse = simpleCheck(type+"_parse");
            dataRate = ( mUnit_download.getDataRate() * mUnit_download.getDataNum() + mUnit_parse.getDataRate() * mUnit_parse.getDataNum() ) /
                    ( mUnit_download.getDataNum() + mUnit_parse.getDataNum());
        }
        return dataRate;
    }

    /**
     * 功能：按照时间监控下载数据，下载数据速度：15.8个/h
     * @param type  :GFS_download、GFS_parse、
     * @return
     */
    @Override
    public MUnit simpleCheck(String type) {
        mUnit = new MUnit();
        Channel channel = null;
        ChannelSftp sftp = null;
        Session session = null;
        float dataRate = 0;
        try {
            session = SFTPUtil.getConnection(PropertyUtil.getProperty(type+"_sftp_host") ,
                    PropertyUtil.getProperty(type+"_sftp_username"),
                    PropertyUtil.getProperty(type+"_sftp_password") ,
                    Integer.parseInt(PropertyUtil.getProperty(type+"_sftp_port")),
                    Integer.parseInt(PropertyUtil.getProperty(type+"_sftp_timeout")) );
            channel = session.openChannel("sftp");
            channel.connect();
            sftp =(ChannelSftp) channel;
            String path = getSSHPath(type);

            dataRate = handlerLoss(path , sftp , type ,mUnit.getJson());
            mUnit.setDataRate(dataRate);
        } catch (JSchException e) {
            logger.error("查询数据物理地址错误");
        }finally {
            session.disconnect();
            channel.disconnect();
        }
        return mUnit;
    }

    /**
     * 功能：根据数据类型获取数据文件路径
     * @param type
     * @return
     */
    private String getSSHPath(String type){
        String path = null;
        if (type.contains("_download")){
            path = PropertyUtil.getProperty("GFS_download_output") + date;
        }
        if (type.contains("_parse")){
            path = PropertyUtil.getProperty("GFS_parse_output") + date;
        }
        if (type.contains("HY1SUO")){
            if (date.substring(8,10).equals("00"))date = NumModelTimeUtils.getGFSDateTime(NumModelTimeUtils.getDateTime() , 10);
            path = PropertyUtil.getProperty("HY1SUO_download_output") + date.substring(0,4) + "/" + date.substring(4,6) + "/" + date.substring(6,8) + "/" +date + "/";
        }
        if (type.contains("WAVEWATCH")){
            path = PropertyUtil.getProperty("WAVEWATCH_download_output") + "gwes." + date.substring(0,8);
        }
        return path;
    }

    /**
     * 根据 文件名+个数 判断缺少情况,并且写入json
     */
    private float handlerLoss(String path , ChannelSftp sftp ,String type ,JSONObject json){
        float dataRate = 0;
        /**********************************
         * GFS_download   GFS_parse
         *********************************/
        // TODO: 2018/7/11 GFS数据个数写死了 
        if (type.contains("GFS_download") || type.contains("GFS_parse")) {
            List<String> fileNames = null;
            if (type.contains("GFS_download")) {
                fileNames = SFTPUtil.getOnlyFileNames(path, sftp, "f");
            }
            if (type.contains("GFS_parse")) {
                fileNames = SFTPUtil.getOnlyFileNames_JSYB(path, sftp, "dat");
            }
            float[] dataRateAndNum = NumModelTimeUtils.getGFSDataRate(fileNames.size() , NumModelTimeUtils.getDateTime());
            if(dataRateAndNum[0] == 100){
                return dataRateAndNum[0];
            }else {
                dataRate = dataRateAndNum[0];
            }
            int fileNum = (int) dataRateAndNum[1];
            //gfs的具体存入逻辑方式,存入json
            DataCheckUtil.GFSDownloadCheck(fileNum , fileNames , type ,json ,date);
        }
        /**********************************
         * HY1SUO
         *********************************/
        if (type.contains("HY1SUO")){
            List<String> fileNames = null;
            fileNames = SFTPUtil.getOnlyFileNames(path, sftp, "png");
            if (fileNames.size() != 6560/2){            // TODO: 2018/7/19 里面的具体检查还没做
                json.put("HY1SUO_download" , 0);
                dataRate = 50;
            }else dataRate = 100;
        }
        /**********************************
         * WAVEWATCH
         *********************************/
        if (type.contains("WAVEWATCH")){
            List<String> fileNames = null;
            fileNames = SFTPUtil.getOnlyFileNames(path, sftp, "grib2");
            if (fileNames.size() != 1){            // TODO: 2018/7/19 里面的图片还没查
                json.put("HY1SUO_download" , 0);
                dataRate = 0;
            }else dataRate = 100;
        }
        return dataRate;
    }

    public MUnit getmUnit() {
        return mUnit;
    }

    public void setmUnit(MUnit mUnit) {
        this.mUnit = mUnit;
    }
}
