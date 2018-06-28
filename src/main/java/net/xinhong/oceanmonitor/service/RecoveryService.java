package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.tools.PropertiesConfig;
import net.xinhong.oceanmonitor.common.tools.RecoveryUtils;
import net.xinhong.oceanmonitor.message.MessageProducerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService {

    public JSONObject resendMessages(String type, String runtime, String[] vtis) {
        JSONObject resJson = new JSONObject();
        String virtualhost = PropertiesConfig.config.getProperty("apollo.apihost");
        String host = PropertiesConfig.config.getProperty("apollo.host");
        String user = PropertiesConfig.config.getProperty("apollo.user");
        String passwd = PropertiesConfig.config.getProperty("apollo.passwd");
        String port = PropertiesConfig.config.getProperty("apollo.port");
        String apiport = PropertiesConfig.config.getProperty("apollo.apiport");
        String queue = PropertiesConfig.config.getProperty(type + ".queue");
        if (virtualhost == null || host == null || user == null || passwd == null || port == null || apiport == null || queue == null) {
            resJson.put("code", 303);
            return resJson;
        }
        if (!port.matches("\\d+") || !apiport.matches("\\d+") || port.length() > 5 || apiport.length() > 5) {
            resJson.put("code", 303);
            return resJson;
        }
        JSONArray array = new JSONArray();
        for (String vti : vtis) {
            JSONObject obj = MessageProducerFactory.createMessageProducer(type).createMessage(runtime, vti);
            if (obj != null) array.add(obj);
        }
        boolean success = RecoveryUtils.rePublishMessage(virtualhost, host, user, passwd, Integer.valueOf(port),
                Integer.valueOf(apiport), queue, array);
        if (success)
            resJson.put("code", 0);
        else
            resJson.put("code", 300);
        return resJson;

    }
}
