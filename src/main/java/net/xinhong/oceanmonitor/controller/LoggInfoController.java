package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.service.HardWareService;
import net.xinhong.oceanmonitor.service.LogInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wingsby on 2018/3/16.
 */
@Controller
@RequestMapping("/loginfo")
public class LoggInfoController {
    private static final Log logger = LogFactory.getLog(LoggInfoController.class);
    @Autowired
    private LogInfoService iService;

    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getLoadAveage(HttpServletRequest request, HttpServletResponse response) {
        String time = request.getParameter("time");
        if (time == null || time.isEmpty())
            time = "1";
        long tt = System.currentTimeMillis();
        try {
            JSONObject resJSON = iService.getInfo(time);
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
}
