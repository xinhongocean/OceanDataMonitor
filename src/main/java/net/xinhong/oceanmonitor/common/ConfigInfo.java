package net.xinhong.oceanmonitor.common;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component("configInfo")
public class ConfigInfo {
    @Resource
    private Map<String, String> properties;

    @Resource
    private Map<String, String> dataProperties;

//    public List<String>getTypes(){
//        Set<String> keys = properties.keySet();
//        for (String str : keys) {
//            if (str.contains(".log")||str.contains(".start")||
//                    str.contains(".chname"))
//                return properties.get(str);
//        }
//    }


    public String getLogPath(String type) {
        Set<String> keys = properties.keySet();
        for (String str : keys) {
            if (str.contains(type) && str.contains(".log"))
                return properties.get(str);
        }
        return null;
    }

    public String getExePath(String type) {
        Set<String> keys = properties.keySet();
        for (String str : keys) {
            if (str.contains(type) && str.contains(".start.exe"))
                return properties.get(str);
        }
        return null;
    }

    public String getDataPath(String type) {
        Set<String> keys = properties.keySet();
        for (String str : keys) {
            if (str.contains(type) && str.endsWith(".data"))
                return properties.get(str);
        }
        return null;
    }


    public Long getFileLimitSize(String filename) {
        Set<String> keys = dataProperties.keySet();
        for (String str : keys) {
            if (str.endsWith(".pattern")) {
                String patternRegex = dataProperties.get(str);
                if (filename.matches(patternRegex)) {
                    String psizeKey = str.replace(".pattern", ".size");
                    if (dataProperties.containsKey(psizeKey)) {
                        String strSize = dataProperties.get(psizeKey);
                        if (strSize.matches("\\d+?"))
                            return Long.parseLong(strSize);
                    }
                }
            }
        }
        return null;
    }

    public int getDownFileNum(String type) {
        Set<String> keys = properties.keySet();
        for (String str : keys) {
            if (str.startsWith(type) && str.endsWith(".filenum")) {
                if (properties.containsKey(str) && properties.get(str).matches("\\d+?"))
                    return Integer.parseInt(properties.get(str));
            }
        }
        return -1;
    }

    public String getCHNName(String type) {
        if (type.endsWith(".log")) type = type.replace(".log", "");
        if (type.endsWith(".data")) type = type.replace(".data", "");
        if (type.endsWith(".exe")) type = type.replace(".exe", "");
        Set<String> keys = properties.keySet();
        for (String str : keys) {
            if (str.contains(type) && str.endsWith(".cname"))
                return properties.get(str);
        }
        return null;
    }

    public List<String> getExeTypes(String machine) {
        Map<String, String> map = getTypeMachines();
        List<String> types = new ArrayList<>();
        Set<String> keys = properties.keySet();
        for (String str : keys) {
            if (str.endsWith(".start.exe")) {
                int idx = str.lastIndexOf(".start.exe");
                String tmp = str.substring(0, idx);
                if (map.containsKey(tmp)) {
                    if (map.get(tmp).equals(machine))
                        types.add(str);
                }
            }
        }
        return types;
    }

    public List<String> getLogTypes(String machine) {
        Map<String, String> map = getTypeMachines();
        List<String> types = new ArrayList<>();
        Set<String> keys = properties.keySet();
        for (String str : keys) {
            if (str.endsWith(".log")) {
                int idx = str.lastIndexOf(".log");
                String tmp = str.substring(0, idx);
                if (map.containsKey(tmp)) {
                    if (map.get(tmp).equals(machine))
                        types.add(str);
                }
            }
        }
        return types;
    }

    public Map<String, String> getTypeMachines() {
        Set<String> keys = properties.keySet();
        Map<String, String> machines = new HashMap<>();
        for (String str : keys) {
            if (str.endsWith("machine")) {
                int idx = str.lastIndexOf(".machine");
                String tmp = str.substring(0, idx);
                machines.put(tmp, properties.get(str));
            }
        }
        return machines;
    }
}