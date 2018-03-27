package net.xinhong.oceanmonitor.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wingsby on 2018/3/20.
 */
public class LogOut {

    public static List<String> getCMDMessage(String cmd){
        StringBuilder sb=new StringBuilder();
        List<String> list=null;
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            InputStreamReader ir=new InputStreamReader(process.getInputStream());
            BufferedReader input=new BufferedReader(ir);
            String line=null;
            list=new ArrayList<>();
            while((line=input.readLine())!=null){
                sb.append(line);
//                sb.append('\n');
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
