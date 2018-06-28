package net.xinhong.oceanmonitor.message;

import com.alibaba.fastjson.JSONObject;

public interface DataMessageProducer {

      JSONObject createMessage(String runtime,String vti);
}
