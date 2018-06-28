package net.xinhong.oceanmonitor.message;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.tools.PropertiesConfig;

public class GFSMessageProducer implements DataMessageProducer {

    private static volatile DataMessageProducer instance;

    public static DataMessageProducer getInstance(){
        if(instance==null){
            synchronized (GFSMessageProducer.class){
                if(instance==null)
                    instance=new GFSMessageProducer();
            }
        }
        return instance;
    }


    private GFSMessageProducer(){

    }


    @Override
    public JSONObject createMessage(String runtime, String vti) {
        if(runtime==null||(runtime!=null&&(!runtime.matches("\\d+")||runtime.length()!=10))
                ||vti==null||(vti!=null&&!vti.matches("\\d+")))
            return null;
        JSONObject obj = new JSONObject();
        String date = runtime.substring(0,8);
        String runhour = runtime.substring(8);
        String name = "gfs.t" + runhour + "z.pgrb2.0p25.f" + String.format("%03d", Integer.valueOf(vti));
        String path = PropertiesConfig.config.getProperty("gfs.uploadpath") + "/" + date + runhour + "/" +
                date + runhour + "_" + name;
        obj.put("date", date + runhour + vti);
        obj.put("path", path);
        return obj;
    }
}
