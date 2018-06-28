package net.xinhong.oceanmonitor.common.tools;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

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


    /**
     * 功能：给出url，返回json
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
//            JSONObject json = new JSONObject(jsonText);
            JSONObject json = JSONObject.parseObject(jsonText);
            return json;
        } finally {
            is.close();
            // System.out.println("同时，从这里也能看出 即便return了，仍然会执行finally的！");
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
