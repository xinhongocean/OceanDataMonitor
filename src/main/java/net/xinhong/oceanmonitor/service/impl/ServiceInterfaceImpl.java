package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.dao.impl.ServiceInterfaceElement;
import net.xinhong.oceanmonitor.service.ServiceInterface;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/26.
 */
@Service
public class ServiceInterfaceImpl implements ServiceInterface,Serializable {
    private JSONObject json = new JSONObject();
    public ServiceInterfaceImpl(){

    }
    /**
     * 功能：监测所有接口json，
     * 监测方式，查看返回的json中的某一个要素值
     */
    public void check(){
        if (!json.isEmpty()) {
            json.clear();
        }
        boolean flag = true;
        for (ServiceInterfaceElement element:ServiceInterfaceElement.values()) {
            flag = simpleCheck(element);
            if (!flag) {
                json.put(element.getType() + "----" + element.getcName(), 0);
            }
        }
    }
    @Override
    public void check(String type){
        if (!json.isEmpty()) {
            json.clear();
        }
        boolean flag = true;
        for (ServiceInterfaceElement element:ServiceInterfaceElement.values()) {
            if(!element.getType().equals(type)) {
                continue;
            }
            flag = simpleCheck(element);
            if (!flag) {
                json.put(element.getType() + "----" + element.getcName(), 0);
            }
        }
    }

    /**
     *
     * @param element   定义的接口枚举类
     * @return  false 表示没有对应数据
     *
     * //实况资料、站点城市预报资料、站点信息、航班及航线信息、民航实况及预报报文查询、台风及火山灰、
     * （云图，雷达，日本传真，欧洲预报，pm2.5）、气候产品、GFS、葵花8相关服务接口、等值线颜色属性查询
     * 其他、图例图片、redis信息查询
     */
    private boolean simpleCheck(ServiceInterfaceElement element){
        JSONObject json = null;
        try {
            json = JSONUtil.readJsonFromUrl(element.geturl());
            if (json==null)return false;
            return judgeData(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    private boolean judgeData(JSONObject json){
        String jsonObject =  json.get("data").toString();
        return (!(jsonObject ==null ||jsonObject.isEmpty() ||jsonObject.length()<5 ))?true:false;
    }
}
