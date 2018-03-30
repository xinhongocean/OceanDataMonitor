package net.xinhong.oceanmonitor.common;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wingsby on 2018/3/20.
 */
public class LogOut {
    static final Logger logger=Logger.getLogger(LogOut.class);

    public static List<String> getCMDMessage(String cmd){
        StringBuilder sb=new StringBuilder();
        List<String> list=new ArrayList<>();
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            InputStreamReader ir=new InputStreamReader(process.getInputStream());
            BufferedReader input=new BufferedReader(ir);
            String line=null;
            while((line=input.readLine())!=null){
                sb.append(line);
                list.add(line);
                logger.error(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
