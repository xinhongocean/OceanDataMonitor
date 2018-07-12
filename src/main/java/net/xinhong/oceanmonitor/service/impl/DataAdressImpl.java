package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.*;
import net.xinhong.oceanmonitor.common.PropertyUtil;
import net.xinhong.oceanmonitor.common.ftp.SFTPUtil;
import net.xinhong.oceanmonitor.common.tools.NumModelTimeUtils;
import net.xinhong.oceanmonitor.service.DataAdress;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Handler;

/**
 * 功能：
 * type：GFS_download、GFS_parse、
 * 注意：由于数据下载速度不定，下载进度可能超过 100%
 *      JSYB的数据比较多,使用DB数据代替查询整个
 * Created by Administrator on 2018/7/3.
 */
@Service
public class DataAdressImpl implements DataAdress ,Serializable{
    private  final Logger logger = LoggerFactory.getLogger(RedisKeyServiceImpl.class);
    private JSONObject json = new JSONObject();
    DecimalFormat decimalFormat000 = new DecimalFormat("000");
    private DateTime dateTime = new DateTime();                 //当前gfs下载时间
    private final static float GFS_DOWNLOAD_SPEED = 16.1f;      //gfs下载速度（6h全部,最低时间:10h）
    private String date = NumModelTimeUtils.getGFSDateTime(dateTime , 0);
    @Override
    public float check(String type) {
        float dataRate = checkSimple(type);
        return dataRate;
    }
    @Override
    public Map<String , Float> checks(String type) {
        Map<String , Float> map = new HashMap<>();
        if (type.equals("GFS")){
            float dataRate = checkSimple(type+"_download");
            map.put(type+"_download" ,dataRate );
            dataRate = checkSimple(type+"_parse");
            map.put(type+"_parse" ,dataRate );
        }
        return map;
    }

    /**
     * 功能：按照时间监控下载数据，下载数据速度：15.8个/h
     * @param type  :GFS_download、GFS_parse、
     * @return
     */
    private float checkSimple(String type){
        Channel channel = null;
        ChannelSftp sftp = null;
        Session session = null;
        float dataRate = 0;
        try {
            System.out.println(PropertyUtil.getProperty(type+"_sftp_host")+"----"+PropertyUtil.getProperty(type+"_sftp_username")+PropertyUtil.getProperty(type+"_sftp_password")
                    );
            System.out.println(Integer.parseInt(PropertyUtil.getProperty(type+"_sftp_port")));
            System.out.println(Integer.parseInt(PropertyUtil.getProperty(type+"_sftp_timeout")));
            session = SFTPUtil.getConnection(PropertyUtil.getProperty(type+"_sftp_host") ,
                    PropertyUtil.getProperty(type+"_sftp_username"),
                    PropertyUtil.getProperty(type+"_sftp_password") ,
                    Integer.parseInt(PropertyUtil.getProperty(type+"_sftp_port")),
                    Integer.parseInt(PropertyUtil.getProperty(type+"_sftp_timeout")) );
            channel = session.openChannel("sftp");
            channel.connect();
            sftp =(ChannelSftp) channel;
            String path = getSSHPath(type);

            dataRate = handlerLoss(path , sftp , type);


        } catch (JSchException e) {
            logger.error("查询数据物理地址错误");
        }finally {
            session.disconnect();
            channel.disconnect();
        }
        return dataRate;
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
        return path;
    }

    /**
     * 根据 文件名+个数 判断缺少情况,并且写入json
     *
     */
    private float handlerLoss(String path , ChannelSftp sftp ,String type){
        float dataRate = 0;
        /**
         * GFS_download   GFS_parse
         */
        // TODO: 2018/7/11 GFS数据个数写死了 
        if (type.contains("GFS_download") || type.contains("GFS_parse")) {
            List<String> fileNames = null;
            if (type.contains("GFS_download")) {
                fileNames = SFTPUtil.getOnlyFileNames(path, sftp, "f");
            }
            if (type.contains("GFS_parse")) {
                fileNames = SFTPUtil.getOnlyFileNames_JSYB(path, sftp, "dat");
            }
            if (fileNames.size() == 97 )return 100;                                                //gfs数据下载个数(73+24)
            String download_date = NumModelTimeUtils.getGFSDateTimeVTI(dateTime ,0);
            int download_hour = Integer.parseInt(download_date.substring(download_date.length()-3));//下载时间
            int fileNum = (download_hour > 10)? 97 : (int)(download_hour * GFS_DOWNLOAD_SPEED) ;    //下载时间内的文件数
            dataRate =  (float) fileNames.size()/ fileNum;
            //存入json
            if (fileNum >73) {
                for (int i = 0; i < 73; i++) {
                    write2json(fileNames , i ,type);
                }
                for (int i = 75; i < 145; i+=3) {
                    write2json(fileNames , i , type);
                }
            }else {
                for (int i = 0; i < 73; i++) {
                    write2json(fileNames , i , type);
                }
            }
        }
        return dataRate;
    }
    //遍历获取的文件名列表,不存在即写入json
    private void write2json(List<String> fileNames ,int i ,String type) {
        String fileName = null;
        if(type.contains("GFS_download")) {
            fileName = date + "_gfs.t12z.pgrb2.0p25.f" + decimalFormat000.format(i);
        }
        if(type.contains("GFS_parse")) {
            fileName = "XHGFS_G_DB_"+date + decimalFormat000.format(i) +".dat";
        }
        boolean flag = false;
        for (String name : fileNames) {
            if (name.equals(fileName)) {
                flag = true;
                break;
            }
        }
        if (flag == false)
            json.put("lossFile--" + fileName, 0);
    }


    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
