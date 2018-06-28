package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.tools.JSONUtil;
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


    @RequestMapping(value = "/firstQuery", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void firstQuery(HttpServletRequest request, HttpServletResponse response) {
        long tt = System.currentTimeMillis();
        JSONObject resJSON = service.query();
//        logger.debug("终止程序耗时:" + (System.currentTimeMillis() - tt));
        resJSON.put("delay", (System.currentTimeMillis() - tt));
        resJSON.put("code", resJSON.get("code"));
        JSONUtil.writeJSONToResponse(response, resJSON);
    }

    public JSONObject firstQuery() {
        return  service.query();
    }



    @RequestMapping(value = "/startall", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void startAllExe(HttpServletRequest request, HttpServletResponse response) {
        String pwd = request.getParameter("password");
        if(!pwd.trim().equals("82193302")){
            return;
        }
        String[]types=new String[]{
                "envform.start.exe","jpwave.start.exe","cncur.start.exe","cnwind.start.exe",
                "cntemp.start.exe","cnwave.start.exe","cnsalt.start.exe","cnreal.start.exe",
                "hycom.down.start.exe","wavewatch.down.start.exe","gtspp.down.start.exe",
                "hycom.process.start.exe","wavewatch.process.start.exe","gtspp.process.start.exe"
        };

        try {
            for(String type:types) {
                JSONObject resJSON = service.start(type);
//                logger.debug("启动耗时:" + (System.currentTimeMillis() - tt));
//                resJSON.put("delay", (System.currentTimeMillis() - tt));
                resJSON.put("code", ResStatus.SUCCESSFUL.getStatusCode());
                JSONUtil.writeJSONToResponse(response, resJSON);
            }
        } catch (Exception e) {
            logger.error("启动失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }


    @RequestMapping(value = "/start", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void startExe(HttpServletRequest request, HttpServletResponse response) {
        String pwd = request.getParameter("password");
        if(!pwd.trim().equals("82193302")){
            return;
        }
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "hycom.process";
        long tt = System.currentTimeMillis();
        type+=".start.exe";
        try {
            JSONObject resJSON = service.start(type);
            logger.debug("启动耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SUCCESSFUL.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("启动失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }

    @RequestMapping(value = "/end", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void endExe(HttpServletRequest request, HttpServletResponse response) {
        String pwd = request.getParameter("password");
        if(!pwd.trim().equals("82193302")){
            return;
        }
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "hycom.process";
        long tt = System.currentTimeMillis();
        type+=".start.exe";
        try {
            JSONObject resJSON = service.stop(type);
            logger.debug("终止程序耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", resJSON.get("code"));
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("终止程序失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code",ResStatus.SEARCH_ERROR.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }

    @RequestMapping(value = "/status", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getStatus(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "hycom.process";
        long tt = System.currentTimeMillis();
        type+=".start.exe";
        try {
            JSONObject resJSON = service.status(type);
            logger.debug("终止程序耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", resJSON.get("code"));
            resJSON.put("message",resJSON.get("message"));
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("终止程序失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR.getStatusCode());
            resJSON.put("message","failed");
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }
}
