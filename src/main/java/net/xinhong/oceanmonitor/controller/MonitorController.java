package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.tools.JSONUtil;
import net.xinhong.oceanmonitor.common.ResStatus;
import net.xinhong.oceanmonitor.service.impl.MonitorInfoServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/6/22.
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController {
    private static final Log logger = LogFactory.getLog(MonitorController.class);
//    MonitorInfoServiceImpl monitorInfoService = new MonitorInfoServiceImpl();

    @Autowired
    MonitorInfoServiceImpl monitorInfoService;
    @RequestMapping(value = "/GFS" , method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getGFSMonitor(HttpServletRequest request, HttpServletResponse response){
        try {
            JSONObject resJSON = monitorInfoService.getInfo("GFS");
            JSONUtil.writeJSONToResponse(response, resJSON);
        }catch (Exception e){
            logger.error("硬件信息查询失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }
}
