package net.xinhong.oceanmonitor.service;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.dao.HardWareDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wingsby on 2018/3/30.
 */
@Service
public class HardWareServiceImpl implements HardWareService {
    @Autowired
    private HardWareDAO dao;
    @Override
    public JSONObject getInfo(String time,String machine) {
        return dao.getInfo(time,machine);
    }
}
