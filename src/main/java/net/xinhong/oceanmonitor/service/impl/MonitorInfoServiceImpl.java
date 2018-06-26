package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.dao.MonitorInfoDAO;
import net.xinhong.oceanmonitor.dao.impl.MonitorInfoDAOImpl;
import net.xinhong.oceanmonitor.service.MonitorInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/21.
 */
@Service
public class MonitorInfoServiceImpl implements MonitorInfoService ,Serializable {
    private  final Logger logger = LoggerFactory.getLogger(MonitorInfoServiceImpl.class);
    public MonitorInfoServiceImpl() {
    }

    MonitorInfoDAO dao = new MonitorInfoDAOImpl();
    @Override
    public JSONObject getInfo(String source) {
        JSONObject jsonObject = dao.getInfo(source);
        logger.info(String.valueOf(jsonObject));
        return jsonObject;
    }

    @Override
    public JSONObject getDownloadInfo(String source) {
        JSONObject jsonObject = dao.getInfo(source);
        JSONObject jsonObject_download = jsonObject.getJSONObject("Download");
        logger.info(String.valueOf(jsonObject));
        return jsonObject_download;
    }

    @Override
    public JSONObject getParseInfo(String source) {
        JSONObject jsonObject = dao.getInfo(source);
        JSONObject jsonObject_parse = jsonObject.getJSONObject("Parse");
        logger.info(String.valueOf(jsonObject));
        return jsonObject_parse;
    }

    public Logger getLogger() {
        return logger;
    }

    public MonitorInfoDAO getDao() {
        return dao;
    }

    public void setDao(MonitorInfoDAO dao) {
        this.dao = dao;
    }
}
