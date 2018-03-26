package net.xinhong.oceanmonitor.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xiaoyu on 16/4/19.
 */
public final class JSONUtil {
    private static final Log logger = LogFactory.getLog(JSONUtil.class);
    private JSONUtil(){}

    public static void writeJSONToResponse(HttpServletResponse response, JSONObject resJSON){
        try {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            logger.debug(resJSON.toString());
            out.write(resJSON.toString());
            out.flush();
            System.out.println("hello");
        } catch (IOException e) {
            logger.error( e);
        }
    }

    public static void writeJSONToResponseDomain(HttpServletResponse response, JSONObject resJSON){
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            PrintWriter out = response.getWriter();
            logger.debug(resJSON.toString());
            out.write(resJSON.toString());
            out.flush();
        } catch (IOException e) {
            logger.error( e);
        }
    }
}
