package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.ConfigInfo;
import net.xinhong.oceanmonitor.common.ResStatus;
import net.xinhong.oceanmonitor.dao.ExcutorOperationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wingsby on 2018/3/20.
 */
@Repository
public class ExcutorOperationDaoImpl implements ExcutorOperationDao {
    @Autowired
    private ConfigInfo info;


    public JSONObject start(String type) {
        JSONObject jsonRes = new JSONObject();
        try {
            String path = info.getExePath(type);
            int indentify=getIndentify(type);
            String relative=ExcutorOperationDaoImpl.class.getClassLoader().
                    getResource("").getPath();
            String cmd = "sh "+relative+"/start.sh" + " "+indentify+" " + path;
            System.out.println(cmd);
            exeCMD(cmd,null);
            jsonRes.put("code", ResStatus.SUCCESSFUL.getStatusCode());
            return jsonRes;
        } catch (Exception e) {
            e.printStackTrace();
            jsonRes.put("code",ResStatus.SEARCH_ERROR.getStatusCode());
            return jsonRes;
        }
    }


    public JSONObject stop(String type) {
        JSONObject jsonRes = new JSONObject();
        try {
            String path = info.getExePath(type);
            String exeName = new File(path).getName();
            String relative=ExcutorOperationDaoImpl.class.getClassLoader().
                    getResource("").getPath();
            String cmd = "sh "+relative+"/stop.sh" + " " + exeName;
            System.out.println(cmd);
            boolean flag=exeCMD(cmd,null);
            if(flag)
                jsonRes.put("code", ResStatus.SUCCESSFUL.getStatusCode());
            else  jsonRes.put("code",ResStatus.SEARCH_ERROR.getStatusCode());
            return jsonRes;
        } catch (Exception e) {
            e.printStackTrace();
            jsonRes.put("code",ResStatus.SEARCH_ERROR.getStatusCode());
            return jsonRes;
        }
    }

    @Override
    public JSONObject status(String type) {
        JSONObject jsonRes = new JSONObject();
        try {
            String path = info.getExePath(type);
            String exeName = new File(path).getName();
            String relative=ExcutorOperationDaoImpl.class.getClassLoader().
                    getResource("").getPath();
            String cmd = "sh "+relative+"/status.sh" + " " + exeName;
            System.out.println(cmd);
            List<String>out=new ArrayList<>();
            boolean flag=exeCMD(cmd,out);
            if(flag) {
                JSONArray array=new JSONArray();
                array.addAll(out);
                jsonRes.put("message", array);
                jsonRes.put("code", ResStatus.SUCCESSFUL.getStatusCode());
            }else  jsonRes.put("code",ResStatus.SEARCH_ERROR.getStatusCode());
            return jsonRes;
        } catch (Exception e) {
            e.printStackTrace();
            jsonRes.put("code",ResStatus.SEARCH_ERROR.getStatusCode());
            return jsonRes;
        }
    }


    public static boolean exeCMD(String cmd,List<String> resStr) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            String line = null;
            while ((line = input.readLine()) != null) {
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

    public static int getIndentify(String type){
        if(type.contains("testMonitor"))return -1;
        String[] array1=("envform.start.exe,jpwave.start.exe,cncur.start.exe,cnwind.start.exe," +
                "cntemp.start.exe,cnwave.start.exe,cnsalt.start.exe,cnreal.start.exe," +
                "hycom.down.start.exe,wavewatch.down.start.exe,gtspp.down.start.exe").split(",");
        String[] array2="hycom.process.start.exe,wavewatch.process.start.exe,gtspp.process.start.exe".split(",");
        for(String str:array1){
            if(str.equals(type))return 0;
        }
        for(String str:array2){
            if(str.equals(type))return 1;
        }
        return -1;
    }
}