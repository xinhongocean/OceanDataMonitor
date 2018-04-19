package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.ConfigInfo;
import net.xinhong.oceanmonitor.common.LinuxCommander;
import net.xinhong.oceanmonitor.dao.ExcutorOperationDao;
import net.xinhong.oceanmonitor.dao.LogInfoDao;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wingsby on 2018/3/20.
 */
@Repository
public class LogInfoDaoImpl implements LogInfoDao {
    Logger logger = Logger.getLogger(LogInfoDaoImpl.class);
    @Autowired
    private ConfigInfo info;

    @Autowired
    private ExcutorOperationDao edao;

    public JSONObject getInfo(String type, String lines, String pwd) {
        String path = info.getLogPath(type);
        String cname = info.getCHNName(type);
        String cmd = "tail -n " + lines + " " + path;
//        if (pwd != null && !pwd.isEmpty()) {
////            cmd += "|grep -E \"" + pwd.toUpperCase()+"|"
////            +pwd.toLowerCase()+"\"";
//            cmd+="|grep " + pwd.toUpperCase();
//        }
        if(pwd!=null&&pwd.length()>0) {
            String relative = LogInfoDaoImpl.class.getClassLoader().
                    getResource("").getPath();
            String shpath = relative + "/log.sh";
            cmd = "sh " + shpath + " " + lines + " " + path + " " + pwd.toUpperCase();
        }
        JSONObject resJson = new JSONObject();
        try {
            List<String> resStr = LinuxCommander.getCMDMessage(cmd);
            logger.error(resStr);
            JSONArray array = new JSONArray();
            array.addAll(resStr);
            resJson.put(type, array);
            resJson.put("message",array);
            resJson.put("type", type);
            resJson.put("path", path);
            resJson.put("cname", cname);
            long time = new File(path).lastModified();
            if (time > 0) {
                resJson.put("time", time);
                resJson.put("timestring", new DateTime(time).toString("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            logger.error("读取日志文件失败");
        }
        return resJson;
    }



    public JSONObject errorInfo(String type) {
        String path = info.getLogPath(type);
        String cname = info.getCHNName(type);
        String relative=LogInfoDaoImpl.class.getClassLoader().
                getResource("").getPath();
        String shpath=relative+"/log.sh";
//        String cmd = "tail -n 1000 " + path+" grep -E \"ERROR|error\" ";
        String cmd="sh "+shpath+" 1000 "+path+" ERROR";
        JSONObject resJson = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            List<String> resStr = LinuxCommander.getCMDMessage(cmd);
            if(resStr!=null&&resStr.size()>0){
                array.addAll(resStr);
                resJson.put("error", 1);
            }else{
                cmd = "tail -n 1 " + path;
                List<String> logstr = LinuxCommander.getCMDMessage(cmd);
                array.addAll(logstr);
                resJson.put("error", 0);
            }
            resJson.put("message",array);
            resJson.put(type, path);
            resJson.put("type", type);
            resJson.put("path", resStr);
            resJson.put("cname", cname);
            long time = new File(path).lastModified();
            if (time > 0) {
                resJson.put("time", time);
                resJson.put("timestring", new DateTime(time).toString("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            logger.error("读取日志文件失败");
        }
        return resJson;
    }

    @Override
    public JSONObject getDownInfo(String type, String year, String month,
                                  String day) {
        String path = info.getDataPath(type);
        String cname = info.getCHNName(type);
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
            List<String> resStr = new ArrayList<>();
            String relative = LogInfoDaoImpl.class.getClassLoader().
                    getResource("").getPath();
            String cmd = "sh " + relative + "/fileinfo.sh" + " " + path;
            logger.error(cmd);
            boolean flag = LinuxCommander.exeCMD(cmd, resStr);
            int i = 0;
            JSONArray array = new JSONArray();
            for (int j = 0; j < resStr.size(); j += 2) {
                Long size = info.getFileLimitSize(resStr.get(j));
                if (size != null && resStr.get(j + 1) != null && resStr.get(j + 1).matches("\\d+?")) {
                    logger.error(resStr.get(j + 1));
                    if (size <= Long.valueOf(resStr.get(j + 1))) {
                        i++;
                        array.add(resStr.get(j));
                    }
                }
            }
            resJson.put("DownloadedFilesNum", i);
            resJson.put("DownloadedFiles", array);
            int num = info.getDownFileNum(type);
            if (num >= 0) resJson.put("TotalFiles", num);
            resJson.put("cname", cname);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return resJson;
    }

    @Override
    public JSONObject firstQuery(String machine) {
        List<String> types = info.getLogTypes(machine);
        JSONObject resJson = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            for (String str : types) {
                JSONObject obj =
                        errorInfo(str.replace(".log",""));
                array.add(obj);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        try {
            List<String> etypes = info.getExeTypes(machine);
            for (String str : etypes) {
                array.add(edao.status(str));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        resJson.put("message", array);
        return resJson;
    }


}
