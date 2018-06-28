package net.xinhong.oceanmonitor.common.tools;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.apache.qpid.amqp_1_0.jms.impl.QueueImpl;
import org.apache.qpid.amqp_1_0.jms.impl.TopicImpl;

import javax.jms.*;
import java.io.IOException;

// wingsby 恢复工具
public class RecoveryUtils {

    static final Logger logger = Logger.getLogger(RecoveryUtils.class);

    //1. 消息;恢复工具,传入重发消息
    public static boolean rePublishMessage(String virtualhost, String host, String user, String passwd, int port, int apiport,
                                           String queue, JSONArray jsonArray) {
        // 例如恢复Gfs消息,读取消息列表保存消息,删除消息,插入消息
        // 重建消息
        String getMessage = "http://" + host + ":" + apiport + "/api/json/broker/virtual-hosts/" + virtualhost
                + "/queues/" + queue + "/messages";
        // 请求命令
        JSONArray messages = null;
        HttpHost target = new HttpHost("123.59.100.107", apiport, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(user, passwd));
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        HttpGet get = new HttpGet(getMessage);
        JSONArray array = null;
        try {
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local
            // auth cache
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(target, basicAuth);
            // Add AuthCache to the execution context
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);
            HttpResponse response = httpClient.execute(target, get, localContext);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                String strRes = EntityUtils.toString(response.getEntity());
                if (strRes != null) {
                    JSONObject jsonObject = JSON.parseObject(strRes);
                    if (!jsonObject.isEmpty())
                        array = (JSONArray) jsonObject.get("messages");
                }
            }
            // 删除命令 没有相应api
            //消费消息
            if (array != null && array.size() > 0)
                consumeMessage(queue, host, user, passwd, port, array.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 重发消息
        if (jsonArray == null) jsonArray = new JSONArray();
        if (array != null) jsonArray.addAll(array);
        int success = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                String msg = jsonArray.getJSONObject(i).toJSONString();
                Publish.pub(msg, queue, true, Publish.DestinationEnum.QUEUE);
                success += 1;
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        if (success >= 0.8 * jsonArray.size())
            return true;
        else return false;
    }


    public static void main(String[] args) {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("id", "up");
        obj.put("type", "gfs");
//        obj.put("ip", ConfigDownload.getApollo_host());
        String date = "20180626";
        String runhour = "12";
        String vti = "001";
        String name = "gfs.t" + runhour + "z.pgrb2.0p25.f" + String.format("%03d", Integer.valueOf(vti));
        String path = PropertiesConfig.config.getProperty("gfs.uploadpath") + "/" + date + runhour + "/" +
                date + runhour + "_" + name;
        obj.put("date", date + runhour + vti);
        obj.put("path", path);
        array.add(obj);
        rePublishMessage("xinhong_mq", "123.59.100.107", "app", "app82193302", 61613,
                61680, "app.test", array);
    }


    public static boolean consumeMessage(String queuename, String host, String user, String password, int port, int size) {
        String destination = "queue://" + queuename;
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host, port, user, password);
        Connection connection = null;
        try {
            connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = null;
            if (destination.startsWith("topic://")) {
                dest = new TopicImpl(destination);
            } else {
                dest = new QueueImpl(destination);
            }
            MessageConsumer consumer = session.createConsumer(dest);
            long count = 0;
            logger.info("Waiting for messages...");
            for (int i = 0; i < size; i++) {
                Message msg = consumer.receive();
                if (msg != null) {
                    if (msg instanceof TextMessage) {
                        String message = ((TextMessage) msg).getText();
                        logger.info("接收消息为:" + message);
                        count++;
                    } else {
                        logger.error("消息不是TEXTMESSAGE类型，请检查程序");
                    }
                } else {
                    logger.warn("消息为空");
                }
            }
            session.close();
            connection.close();
            if (count >= size - 1) return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Apollo 连接失败!");
        } finally {
            return false;
        }
    }

}
