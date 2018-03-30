package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.common.ResStatus;
import net.xinhong.oceanmonitor.common.StringUtils;
import net.xinhong.oceanmonitor.service.HardWareService;
import net.xinhong.oceanmonitor.service.LogInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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
    private static final Logger logger = Logger.getLogger(LoggInfoController.class);
    @Autowired
    private LogInfoService iService;

    @RequestMapping(value = "/tail", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getLogInfo(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        String lines = request.getParameter("line");
        if (type == null || type.isEmpty()) type = "hycom.process";
        if (lines == null || lines.isEmpty()) lines = "30";
        long tt = System.currentTimeMillis();
        type += ".log";
        try {
            JSONObject resJSON = iService.getInfo(type, lines);
            logger.debug("查询耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SUCCESSFUL.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("硬件信息查询失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
//        response.sendRedirect("http://192.168.1.114:8088/");
    }


    @RequestMapping(value = "/downfile", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public final void getDownloadInfo(HttpServletRequest request, HttpServletResponse response) {
        // path 各path有多少文件，其中下载完多少文件
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "hycom.down";
        long tt = System.currentTimeMillis();
        type += ".data";
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");
        DateTime curDate = DateTime.now();
        if(type.startsWith("hycom"))curDate=curDate.minusDays(2);
        else curDate=curDate.minusDays(1);
        if (year==null  || year.isEmpty()) year = StringUtils.leftPad(Integer.toString(curDate.getYear()), 4, '0');
        if (month==null || month.isEmpty()) month = StringUtils.leftPad(Integer.toString(curDate.getMonthOfYear()), 2, '0');;
        if (day==null   || day.isEmpty()) day =  StringUtils.leftPad(Integer.toString(curDate.getDayOfMonth()), 2, '0');
        try {
            JSONObject resJSON = iService.getDownInfo(type,year,month,day);
            logger.debug("查询耗时:" + (System.currentTimeMillis() - tt));
            resJSON.put("delay", (System.currentTimeMillis() - tt));
            resJSON.put("code", ResStatus.SUCCESSFUL.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("硬件信息查询失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }
}
