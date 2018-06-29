package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.dao.impl.ServiceInterfaceElement;
import net.xinhong.oceanmonitor.service.ServiceInterface;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Administrator on 2018/6/26.
 */
@Service
public class ServiceInterfaceImpl implements ServiceInterface {
    private JSONObject json = new JSONObject();
    public ServiceInterfaceImpl(){

    }
    /**
     * 功能：监测所有接口json，
     * 监测方式，查看返回的json中的某一个要素值
     */
    public void check(){
        if (!json.isEmpty())json.clear();
        boolean flag = true;
        for (ServiceInterfaceElement element:ServiceInterfaceElement.values()) {
            flag = simpleCheck(element);
            if (!flag)json.put(element.getType()+"----"+element.getcName(),0);
        }
    }
    public void check(String type){
        if (!json.isEmpty())json.clear();
        boolean flag = true;
        for (ServiceInterfaceElement element:ServiceInterfaceElement.values()) {
            if(!element.getType().equals(type))continue;
            flag = simpleCheck(element);
            if (!flag)json.put(element.getType()+"----"+element.getcName(),0);
        }
    }

    /**
     *
     * @param element   定义的接口枚举类
     * @return  false 表示没有对应数据
     * //实况资料、站点城市预报资料、站点信息、航班及航线信息、民航实况及预报报文查询、台风及火山灰、
     * （云图，雷达，日本传真，欧洲预报，pm2.5）、气候产品、GFS、葵花8相关服务接口、等值线颜色属性查询
     * 其他、图例图片、redis信息查询
     */
    private boolean simpleCheck(ServiceInterfaceElement element){
        JSONObject json = null;
        try {
            json = JSONUtil.readJsonFromUrl(element.geturl());
            if (json==null)return false;

            if (element.name().equals("SKSJ1") ||element.name().equals("SKSJ2")||element.name().equals("SKSJ3") ||element.name().equals("SKSJ5")
                    ||element.name().equals("SKSJ6")||element.name().equals("SKSJ7")||element.name().equals("SKSJ8")
                    ||element.name().equals("ZDCSYBZL1")||element.name().equals("ZDCSYBZL3")||element.name().equals("ZDXX5")
                    ||element.name().equals("ZDXX6")||element.name().equals("HBJHXXX1")){
                JSONObject jsonObject = (JSONObject) json.get("data");
                if (jsonObject == null)return false;
                return (jsonObject.get("TT")!=null ||jsonObject.get("aqidatalist" )!=null ||jsonObject.get("array" )!=null
                        ||jsonObject.get("maxVal" )!=null||jsonObject.get("stationCode" )!=null||jsonObject.get("sta" )!=null

                )?true:false;
            }
                    //data后为{}
            if (element.name().equals("SKSJ4") ||element.name().equals("SKSJ9") ||element.name().equals("SKSJ12")
                    ||element.name().equals("SKSJ10")||element.name().equals("SKSJ11")||element.name().equals("SKSJ14")
                    ||element.name().equals("SKSJ15")||element.name().equals("ZDCSYBZL2")
                    ||element.name().equals("ZDXX4")
                    ||element.name().equals("MHSKJYBBWCX3")
                    ||element.name().equals("YTLD4")
                    ||element.name().equals("QHCP1")
                    ||element.name().equals("GFS1")||element.name().equals("GFS3")||element.name().equals("GFS4")
                    ||element.name().equals("GFS5")||element.name().equals("GFS6")||element.name().equals("GFS7")
                    ||element.name().equals("GFS8")||element.name().equals("GFS9")||element.name().equals("GFS10")
                    ||element.name().equals("GFS11")||element.name().equals("GFS12")||element.name().equals("GFS13")
                    ||element.name().equals("GFS14")||element.name().equals("GFS15")||element.name().equals("GFS16")
                    ||element.name().equals("KH1")||element.name().equals("KH2")||element.name().equals("KH3")
                    ||element.name().equals("KH4")||element.name().equals("KH5")||element.name().equals("DZXYSSXCX1")
                    ||element.name().equals("DZXYSSXCX2")||element.name().equals("DZXYSSXCX3")||element.name().equals("DZXYSSXCX4")
                    ||element.name().equals("DZXYSSXCX5")||element.name().equals("DZXYSSXCX6")||element.name().equals("QT1")
                    ||element.name().equals("REDIS2")||element.name().equals("REDIS3")
                    ||element.name().equals("TLTP1")||element.name().equals("TLTP2")||element.name().equals("TLTP3")
                    ||element.name().equals("TLTP4")||element.name().equals("TLTP5")||element.name().equals("TLTP6")
                    ||element.name().equals("TLTP7")||element.name().equals("TLTP8")||element.name().equals("TLTP9")
                    ||element.name().equals("TLTP10")||element.name().equals("TLTP11")||element.name().equals("TLTP12")
                    ||element.name().equals("TLTP13")||element.name().equals("TLTP14")||element.name().equals("TLTP15")
                    ||element.name().equals("TLTP16")||element.name().equals("TLTP17")||element.name().equals("TLTP18")
                    ){
                JSONObject jsonObject = (JSONObject) json.get("data");
                return (jsonObject !=null )?true:false;
            }
                    //data后为[]
            if (element.name().equals("ZDXX1")
                    ||element.name().equals("ZDXX2") ||element.name().equals("ZDXX3")
                    ||element.name().equals("HBJHXXX2")||element.name().equals("HBJHXXX3")||element.name().equals("HBJHXXX4")
                    ||element.name().equals("HBJHXXX5")||element.name().equals("HBJHXXX6")||element.name().equals("MHSKJYBBWCX1")
                    ||element.name().equals("MHSKJYBBWCX2")||element.name().equals("TFJHSH1")||element.name().equals("TFJHSH3")
                    ||element.name().equals("YTLD1")||element.name().equals("YTLD2")||element.name().equals("YTLD3")
                    ||element.name().equals("YTLD5")||element.name().equals("YTLD6")||element.name().equals("YTLD7")
                    ||element.name().equals("YTLD8")||element.name().equals("YTLD9")||element.name().equals("GFS2")
                    ||element.name().equals("REDIS1")
                    ){
                JSONArray jsonObject = (JSONArray) json.get("data");
                return (!jsonObject.isEmpty() )?true:false;
            }

            if(element.name().equals("SKSJ13") ){  //包含其他变量
                JSONObject jsonObject = (JSONObject) json.get("data");
                if (jsonObject == null)return false;
                JSONObject jsonObject1 = (JSONObject) jsonObject.get("0700");
                if (jsonObject1 == null)return false;
                return (jsonObject1.get("TT")!=null )?true:false;
            }

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
}
