package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.ConfigInfo;
import net.xinhong.oceanmonitor.common.LogOut;
import net.xinhong.oceanmonitor.dao.LogInfoDao;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Created by wingsby on 2018/3/20.
 */
@Repository
public class LogInfoDaoImpl implements LogInfoDao {
    Logger logger = Logger.getLogger(LogInfoDaoImpl.class);
    @Autowired
    private ConfigInfo info;

    public JSONObject getInfo(String type, String lines) {
        String path = info.getLogPath(type);
        String cmd = "tail -n " + lines + " " + path;
        JSONObject resJson = new JSONObject();
        try {
            List<String> resStr = LogOut.getCMDMessage(cmd);
            JSONArray array = new JSONArray();
            array.addAll(resStr);
            resJson.put(type, resStr);
        } catch (Exception e) {
            logger.error("读取日志文件失败");
        }
        return resJson;
    }

    @Override
    public JSONObject getDownInfo(String type, String year, String month,
                                  String day) {
        String path = info.getDataPath(type);
        logger.error(path);
        String date = year + month + day;
        if (type.contains("hycom.down") || type.contains("wavewatch.down")) {
            String[] strs = path.split("@");
            if (strs.length == 2) {
                path = strs[0] + date;
            }
        }
//        String cmd="find "+path +" -type f|wc -l";
//        String cmd = "ls -l " + path; //+ "| awk '{print $9,$5}'";

        JSONObject resJson = new JSONObject();
        try {
            List<String> resStr =new ArrayList<>();
            String relative=LogInfoDaoImpl.class.getClassLoader().
                    getResource("").getPath();
            String cmd = "sh "+relative+"/status.sh" + " " + path;
            logger.error(cmd);
            boolean flag=exeCMD(cmd,resStr);
            int i = 0;
            for (String str : resStr) {
                String[] tmp = str.split(" ");
                System.out.println(str);
                logger.error(str);
                if (tmp != null && tmp.length == 2) {
                    Long size = info.getFileLimitSize(tmp[0]);
                    if (size != null && tmp[1] != null && tmp[1].matches("\\d+?")) {
                        if (size <= Long.valueOf(tmp[1])) {
                            i++;
                        }
                    }
                }
            }
            resJson.put("DownloadedFiles:", i);
            int num = info.getDownFileNum(type);
            if (num >= 0) resJson.put("TotalFiles:", num);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return resJson;
    }

    public  boolean exeCMD(String cmd,List<String> resStr) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            String line = null;
            logger.error(input);
            while ((line = input.readLine()) != null) {
                logger.error(line);
                sb.append(line);
                sb.append('\n');
                if(resStr!=null)resStr.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
