package net.xinhong.oceanmonitor.dao.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.ConfigInfo;
import net.xinhong.oceanmonitor.dao.ExcutorOperationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wingsby on 2018/3/20.
 */
@Repository
public class ExcutorOperationDaoImpl implements ExcutorOperationDao {
    @Autowired
    private ConfigInfo info;

    @Override
    public JSONObject start(String type) {

        return null;
    }

    @Override
    public JSONObject stop(String type) {
        return null;
    }


    public static String getCMDMessage(String cmd){
        StringBuilder sb=new StringBuilder();
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            InputStreamReader ir=new InputStreamReader(process.getInputStream());
            BufferedReader input=new BufferedReader(ir);
            String line=null;
            while((line=input.readLine())!=null){
                sb.append(line);
                sb.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
