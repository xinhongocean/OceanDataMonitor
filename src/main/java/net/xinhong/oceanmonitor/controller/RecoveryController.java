package net.xinhong.oceanmonitor.controller;


import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.tools.NumModelTimeUtils;
import net.xinhong.oceanmonitor.service.RecoveryService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller

public class RecoveryController {

    private static final Logger logger = Logger.getLogger(RecoveryController.class);

    @Autowired
    private RecoveryService service;


    @RequestMapping(value = "/resendMessages", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject resendMessages(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String token = request.getParameter("token");
        if (token == null || (token != null && token.contains("xhgk"))) {
            jsonObject.put("code", "303");
            return jsonObject;
        }
        String strHour = request.getParameter("hours");
        String type = request.getParameter("type");
        String minusHour = request.getParameter("minushour");
        String runtime = request.getParameter("runtime");
        if (minusHour == null || (minusHour != null && !minusHour.matches("\\d+"))) minusHour = "0";
        String[] vtis = null;
//        String runtime = null;
        if (type == null) type = "gfs.down";
        String datetime = null;
        if (runtime == null) {
            datetime = NumModelTimeUtils.getDateTimeVTI(DateTime.now(), Integer.valueOf(minusHour), type);
            if (datetime != null && datetime.length() == 13) runtime = datetime.substring(0, 10);
        }

        if (strHour != null)
            vtis = strHour.split(",");
        else {
            // 最近6个小时
            int vti = Integer.valueOf(datetime.substring(10));
            vtis = new String[6];
            for (int i = 0; i < 6; i++)
                vtis[i] = String.format("%03d", i + vti);
        }
        jsonObject = service.resendMessages(type, runtime, vtis);
        return jsonObject;
    }

}
