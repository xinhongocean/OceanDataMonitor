package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.common.ResStatus;
import net.xinhong.oceanmonitor.service.ExcutorOperationService;
import org.apache.log4j.Logger;
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
@RequestMapping("/operation")
public class ExcutorOperationController {
    private static final Logger logger = Logger.getLogger(ExcutorOperationController.class);
    @Autowired
    private ExcutorOperationService service;

    @RequestMapping(value = "/start", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void startExe(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "hycom";
        long tt = System.currentTimeMillis();
        try {
            JSONObject resJSON = service.start(type);
            logger.debug("启动耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SUCCESSFUL);
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("启动失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR);
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }

    @RequestMapping(value = "/end", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void endExe(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "hycom";
        long tt = System.currentTimeMillis();
        try {
            JSONObject resJSON = service.stop(type);
            logger.debug("终止程序耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SUCCESSFUL);
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("终止程序失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR);
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }
}
