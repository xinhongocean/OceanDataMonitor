package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.dao.LogInfoDao;
import net.xinhong.oceanmonitor.service.LogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wingsby on 2018/3/20.
 */
@Service
public class LogInfoServiceImpl implements LogInfoService {
    @Autowired
    private LogInfoDao dao;

    @Override
    public JSONObject getInfo(String type, String lines) {
        return dao.getInfo(type, lines);
    }

    @Override
    public JSONObject getDownInfo(String type, String year, String month,
                                  String day) {
        return dao.getDownInfo(type, year, month,
                day);
    }
}
