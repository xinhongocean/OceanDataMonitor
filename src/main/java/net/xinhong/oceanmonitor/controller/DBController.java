package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.DBAccess;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.common.ResStatus;
import net.xinhong.oceanmonitor.service.LogInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zxf on 2018/3/26.
 */
@Controller
@RequestMapping("/DBinfo")
public class DBController {
    private static final Logger logger = Logger.getLogger(LoggInfoController.class);
//    @Autowired
//    private LogInfoService iService;

    @RequestMapping(value = "/connectionstatus", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getLogInfo(HttpServletRequest request, HttpServletResponse response) {
        long tt = System.currentTimeMillis();


        try {
            DBAccess dbac = new DBAccess();
            JSONObject resJSON = new JSONObject();

            if (dbac.getConnection() != null) {
                logger.info("Get connnection success!");
                resJSON.put("DBStatus",true);
            } else {
                logger.info("Get connnection fail!");
                resJSON.put("DBStatus",false);
            }
            logger.debug("查询耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SUCCESSFUL);
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
        catch (Exception e){
            JSONObject resJSON = new JSONObject();
            logger.debug("查询耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SEARCH_ERROR);
            JSONUtil.writeJSONToResponse(response, resJSON);
        }

    }

}
