package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.RedisUtil;
import net.xinhong.oceanmonitor.common.ResStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wingsby on 2018/3/30.
 */
@Service
public class RedisService {

    @Autowired
    RedisUtil redisUtil;


    //获取当前redis使用内存大小情况
    public JSONObject getMemeryInfo(String machine) {
        Map<String, String> clusterinfo = redisUtil.getRedisInfo(machine);
//        Map<String, Long> nodeSize = redisUtil.getRedisSize();
        JSONObject resJson = new JSONObject();
        List<JSONObject>jsonObjectList=new ArrayList<>();

        for (String node : clusterinfo.keySet()) {
            JSONObject nodeJson = new JSONObject();
            if(clusterinfo.get(node).equals("fail")){
                nodeJson.put("code",ResStatus.SEARCH_ERROR.getStatusCode());
                jsonObjectList.add(nodeJson);
                continue;
            }
            String[] strs = clusterinfo.get(node).split("\n");
            for (int i = 0; i < strs.length; i++) {
                String s = strs[i];
                String[] detail = s.split(":");
                if (detail[0].equals("used_memory")) {
                    nodeJson.put("used_memory", detail[1].substring(0, detail[1].length() - 1));
                    nodeJson.put("create_time", new Date().getTime());
                    nodeJson.put("code",ResStatus.SUCCESSFUL.getStatusCode());
                    break;
                }
            }
//            if (nodeSize.containsKey(node)) {
//                Long size = nodeSize.get(node);
//                nodeJson.put("keysize", size);
//            }
            String[] tmp=node.split(":");
            if(tmp!=null&&tmp.length==2) {
                if(tmp[1].trim().matches("\\d+"))
                    nodeJson.put("port", Integer.valueOf(tmp[1]));
            }
            jsonObjectList.add(nodeJson);
//            resJson.put(node, nodeJson);
        }
        jsonObjectList.sort(new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                if(o1.get("port")==null)return -1;
                if(o2.get("port")==null)return 1;
                if((Integer)o1.get("port")>(Integer)o2.get("port")){
                    return 1;
                }else return -1;
            }
        });

        JSONArray array=new JSONArray();
        array.addAll(jsonObjectList);
        resJson.put("nodes",array);
        return resJson;
    }

    private String getDateStr(long timeStmp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(timeStmp));
    }
}

