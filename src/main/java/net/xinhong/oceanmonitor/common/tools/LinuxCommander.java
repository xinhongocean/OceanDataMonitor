package net.xinhong.oceanmonitor.common.tools;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wingsby on 2018/3/20.
 */
public class LinuxCommander {
    static final Logger logger=Logger.getLogger(LinuxCommander.class);

    public static List<String> getCMDMessage(String cmd){
        StringBuilder sb=new StringBuilder();
        List<String> list=new ArrayList<>();
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            InputStreamReader ir=new InputStreamReader(process.getInputStream());
            BufferedReader input=new BufferedReader(ir);
            String line=null;
            logger.info(cmd);
            while((line=input.readLine())!=null){
                sb.append(line);
                sb.append('\n');
                list.add(line);
                logger.error(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public  static boolean exeCMD(String cmd,List<String> resStr) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            String line = null;
//            logger.error(input);
            while ((line = input.readLine()) != null) {
//                logger.error(line);
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
