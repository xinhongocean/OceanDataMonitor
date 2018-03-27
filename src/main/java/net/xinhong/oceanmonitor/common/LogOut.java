package net.xinhong.oceanmonitor.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wingsby on 2018/3/20.
 */
public class LogOut {

    public static String getCMDMessage(String cmd){
        StringBuilder sb=new StringBuilder();
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            InputStreamReader ir=new InputStreamReader(process.getInputStream());
            BufferedReader input=new BufferedReader(ir);
            String line=null;
            while((line=input.readLine())!=null){
                sb.append(line);
//                sb.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
