package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONObject;

public abstract class MonitorChain {

    //监控责任链,当监控结果有问题时,提交至下一环,正常时返回正常
    //目的是为了找出错误开始的一环：结果端判断，如果成功，则返回；如果不成功，则提交至下一环
    private MonitorChain next=null;


    public MonitorChain getNext() {
        return next;
    }

    public void setNext(MonitorChain next) {
        this.next = next;
    }

    public abstract boolean isOk(String type);      //满足条件，返回

    public abstract String getErrInfo(String type);

    public JSONObject handleMonitor(String type, JSONObject object) {
        if (object == null) object = new JSONObject();
        if (isOk(type)) {
            return object;
        } else {
            //错误信息
            String errinfo=getErrInfo(type);
            object.put(getClass().getName(),errinfo);
            if(next!=null)
                return next.handleMonitor(type, object);
            else{
                // 每个环节的错误,环节名 todo
                object.put(getClass().getName(),errinfo);
                return object;
            }
        }
    }

}
