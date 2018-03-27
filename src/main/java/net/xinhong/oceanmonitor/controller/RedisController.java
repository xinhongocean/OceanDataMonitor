package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.common.ResStatus;
import net.xinhong.oceanmonitor.service.RedisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zxf on 2018/3/26.
 */
@Controller
@RequestMapping("/redisinfo")
public class RedisController {
    private static final Logger logger = Logger.getLogger(RedisController.class);
    @Autowired
    private RedisService iService;

    @RequestMapping(value = "/singleport", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getLogInfo(HttpServletRequest request, HttpServletResponse response) {
        String portID = request.getParameter("portID");
        if (portID == null || portID.isEmpty()) portID = "6379";

        long tt = System.currentTimeMillis();
        try {
            JSONObject resJSON = iService.getInfo(portID);
            logger.debug("查询耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SUCCESSFUL);
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("硬件信息查询失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR);
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }
}
