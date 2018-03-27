package net.xinhong.oceanmonitor.common;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Component("configInfo")
public class ConfigInfo {
    @Resource
    private Map<String,String> properties;

    public String getLogPath(String type){
        Set<String> keys=properties.keySet();
        for(String str:keys){
            if(str.contains(type)&&str.contains(".log"))
                return properties.get(str);
        }
        return null;
    }

    public String getExePath(String type){
        Set<String> keys=properties.keySet();
        for(String str:keys){
            if(str.contains(type)&&str.contains(".start.exe"))
                return properties.get(str);
        }
        return null;
    }

//    public String getStopPath(String type){
//        Set<String> keys=properties.keySet();
//        for(String str:keys){
//            if(str.contains(type)&&str.contains(".stop.exe"))
//                return properties.get(str);
//        }
//        return null;
//    }

}