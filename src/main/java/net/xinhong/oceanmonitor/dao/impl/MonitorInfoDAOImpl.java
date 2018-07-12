package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.PropertyUtil;
import net.xinhong.oceanmonitor.dao.MonitorInfoDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2018/6/21.
 */
@Repository
public class MonitorInfoDAOImpl implements MonitorInfoDAO{
    Logger logger = Logger.getLogger(MonitorInfoDAOImpl.class);
    public MonitorInfoDAOImpl() {
    }
    static PropertyUtil propertyUtil =new PropertyUtil();
    @Override
    public JSONObject getInfo(String source) {
        loadConf(source);
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject_body = new JSONObject();
        JSONObject jsonObject_download = new JSONObject();
        JSONObject jsonObject_parse = new JSONObject();

        if(source.contains("gfs") || source.contains("GFS")){   //GFS
            jsonObject.put("GFS",jsonObject_body);
            source = "GFS";
        }
        jsonObject_body.put("Download" , jsonObject_download);
        jsonObject_body.put("Parse" , jsonObject_parse);
        jsonObject_download.put("download_input" , propertyUtil.getProperty(source+"_download_input"));
        jsonObject_download.put("download_output" , propertyUtil.getProperty(source+"_download_output"));
        jsonObject_download.put("download_log" , propertyUtil.getProperty(source+"_download_log"));
        jsonObject_download.put("download_location" , propertyUtil.getProperty(source+"_download_location"));
        jsonObject_download.put("download_start" , propertyUtil.getProperty(source+"_download_start"));
        jsonObject_parse.put("parse_input" , propertyUtil.getProperty(source+"_parse_input"));
        jsonObject_parse.put("parse_output" , propertyUtil.getProperty(source+"_parse_output"));
        jsonObject_parse.put("parse_log" , propertyUtil.getProperty(source+"_parse_log"));
        jsonObject_parse.put("parse_location" , propertyUtil.getProperty(source+"_parse_location"));
        jsonObject_parse.put("parse_start" , propertyUtil.getProperty(source+"_parse_start"));

        return jsonObject;
    }
    //加载monitor配置文件
    private void loadConf(String source){
        propertyUtil = new PropertyUtil();
        if(source.contains("gfs") || source.contains("GFS")){   //GFS
            propertyUtil.loadProps("monitorConf/GFSProcess.properties");
        }

        logger.info(propertyUtil);

    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public PropertyUtil getPropertyUtil() {
        return propertyUtil;
    }

    public void setPropertyUtil(PropertyUtil propertyUtil) {
        this.propertyUtil = propertyUtil;
    }

}
