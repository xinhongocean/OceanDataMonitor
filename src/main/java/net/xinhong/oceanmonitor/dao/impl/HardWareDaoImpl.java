package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.LinuxCommander;
import net.xinhong.oceanmonitor.dao.HardWareDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxf on 2018/3/26.
 */
@Repository
public class HardWareDaoImpl implements HardWareDAO {
    Logger logger=Logger.getLogger(HardWareDaoImpl.class);

    @Override
    public JSONObject getInfo(String time) {
        List<String> resStr =new ArrayList<>();
        String relative=LogInfoDaoImpl.class.getClassLoader().
                getResource("").getPath();
        String cmd = "sh "+relative+"/hardware.sh" ;
        boolean flag=LinuxCommander.exeCMD(cmd,resStr);
        JSONObject resJson = new JSONObject();
        int line=resStr.size();
        try {
            resJson.put("harddisk", resStr.get(line-3));
            resJson.put("loadavg", resStr.get(line-2));
            logger.error(resStr.get(line-1));
            if(resStr.get(line-1).matches("\\d+?"))
                resJson.put("cpu", 100-Integer.valueOf(resStr.get(line-1)));
        } catch (Exception e) {
            logger.error("获取监控失败");
        }
        return resJson;
    }
}
