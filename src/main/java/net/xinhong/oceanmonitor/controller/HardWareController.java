package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.service.DBservice;
import net.xinhong.oceanmonitor.service.HardWareService;
import net.xinhong.oceanmonitor.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wingsby on 2018/3/16.
 */
@Controller
@RequestMapping("/hardware")
public class HardWareController {
    private static final Log logger = LogFactory.getLog(HardWareController.class);
    @Autowired
    private HardWareService hService;
    @Autowired
    private RedisService rService;
    @Autowired
    private DBservice dbService;

    @RequestMapping(value = "/info", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getLoadAveage(HttpServletRequest request, HttpServletResponse response) {
        String time = request.getParameter("time");
        if (time == null || time.isEmpty())
            time = "1";
        long tt = System.currentTimeMillis();
        try {
            JSONObject resJSON = hService.getInfo(time);
            logger.debug("查询耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("硬件信息查询失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", "400");
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }


    @RequestMapping(value = "/redisinfo", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getRedisInfo(HttpServletRequest request, HttpServletResponse response) {
        long tt = System.currentTimeMillis();
        try {
            JSONObject resJSON = rService.getInfo();
            logger.debug("查询耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("REDIS信息查询失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", "400");
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }


}
