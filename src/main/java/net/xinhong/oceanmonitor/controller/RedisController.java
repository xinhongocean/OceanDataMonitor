package net.xinhong.oceanmonitor.controller;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.GFSTimeManger;
import net.xinhong.oceanmonitor.common.JSONUtil;
import net.xinhong.oceanmonitor.common.ResStatus;
import net.xinhong.oceanmonitor.service.impl.RedisKeyServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/6/25.
 */
@Controller
public class RedisController {
    private static final Log logger = LogFactory.getLog(MonitorController.class);
    private static RedisKeyServiceImpl timeMangerJob = new RedisKeyServiceImpl();
    //redis监控
    static {
        GFSTimeManger manger =
                new GFSTimeManger(GFSTimeManger.TIME_PER_DAY,
                        new String[]{"991800"}, timeMangerJob);

    }
    @RequestMapping(value = "/redis", method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json;charset=UTF-8")
    public final void getRedisMonitor(HttpServletRequest request, HttpServletResponse response) {
        String date=request.getParameter("date");
        String redisDataRate = Float.toString(timeMangerJob.checkKeys(date)) + "%";
        String redisTime = timeMangerJob.getDate();     //先获取数据，后获取日期
        try {
            JSONObject resJSON = new JSONObject();
            resJSON.put("time" , redisTime);
            resJSON.put("dataRate",redisDataRate);
            JSONUtil.writeJSONToResponse(response, resJSON);
        } catch (Exception e) {
            logger.error("硬件信息查询失败:" + e);
            JSONObject resJSON = new JSONObject();
            resJSON.put("code", ResStatus.SEARCH_ERROR.getStatusCode());
            JSONUtil.writeJSONToResponse(response, resJSON);
        }
    }
}
