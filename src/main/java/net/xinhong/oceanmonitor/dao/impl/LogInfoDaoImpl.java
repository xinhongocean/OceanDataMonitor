package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.ConfigInfo;
import net.xinhong.oceanmonitor.common.LogOut;
import net.xinhong.oceanmonitor.dao.LogInfoDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by wingsby on 2018/3/20.
 */
@Repository
public class LogInfoDaoImpl implements LogInfoDao {
    Logger logger= Logger.getLogger(LogInfoDaoImpl.class);
    @Autowired
    private ConfigInfo info;

    public JSONObject getInfo(String type, String lines) {
        String path= info.getLogPath(type);
        String cmd="tail -n "+lines +" "+path;
        JSONObject resJson=new JSONObject();
        try {
            String resStr = LogOut.getCMDMessage(cmd);
            resJson.put(type,resStr);
        }catch (Exception e){
            logger.error("读取日志文件失败");
        }
        return resJson;
    }
}
