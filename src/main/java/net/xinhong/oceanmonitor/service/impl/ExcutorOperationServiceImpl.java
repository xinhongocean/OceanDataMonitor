package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.dao.ExcutorOperationDao;
import net.xinhong.oceanmonitor.dao.LogInfoDao;
import net.xinhong.oceanmonitor.service.ExcutorOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wingsby on 2018/3/20.
 */
@Service
public class ExcutorOperationServiceImpl implements ExcutorOperationService {
    @Autowired
    private ExcutorOperationDao dao;

    @Override
    public JSONObject start(String type) {
        return dao.start(type);
    }

    @Override
    public JSONObject stop(String type) {
        return dao.stop(type);
    }

    @Override
    public JSONObject status(String type) {
        return dao.status(type);
    }
}
