package net.xinhong.oceanmonitor.common.tools;

import org.apache.log4j.Logger;
import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.apache.qpid.amqp_1_0.jms.impl.QueueImpl;
import org.apache.qpid.amqp_1_0.jms.impl.TopicImpl;

import javax.jms.*;

/**
 * Description: ampp 方式发送消息<br>
 *
 * @author 作者 <a href=ds.lht@163.com>stone</a>
 * @version 创建时间：2016/10/11.
 */

public class Publish {
    private static final Logger logger = Logger.getLogger(Publish.class);
    private static final String user = PropertiesConfig.config.getProperty("apollo.user");
    private static final String password = PropertiesConfig.config.getProperty("apollo.passwd");
    private static final String host = PropertiesConfig.config.getProperty("apollo.host");
    private static int port = 61613;

    static {
        String strport = PropertiesConfig.config.getProperty("apollo.port");
        if (strport.matches("\\d+?")) port = Integer.valueOf(strport);
    }


    public Publish() {
    }

    public static void pub(String msgContent, String topicStr,
                           boolean persistent, DestinationEnum dest) {
        Connection connection = null;
        try {
            ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host, port, user, password);

            Topic topic;
            //topic 方式
            if (dest == DestinationEnum.TOPIC) {
                topic = new TopicImpl(DestinationEnum.TOPIC.getCode() + topicStr);
            } else {
                //queue 方式
                topic = new QueueImpl(DestinationEnum.QUEUE.getCode() + topicStr);
            }
            connection = factory.createConnection(user, password);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(topic);
            if (persistent)
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            else
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage msg = session.createTextMessage(msgContent);
            producer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
            logger.error("消息发送失败！", e);
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }


    public enum DestinationEnum {
        TOPIC("TOPIC", "topic://", "广播方式发送消息，客户端不在线的话，消息会丢失"),
        QUEUE("QUEUE", "queue://", "点对点方式发送，一条消息只能一个客户端接收");


        public String getEname() {
            return ename;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        private String ename;
        private String code;
        private String description;

        DestinationEnum(String ename, String code, String description) {
            this.ename = ename;
            this.code = code;
            this.description = description;
        }

        public static String getCodeByName(String name) {
            for (DestinationEnum dest : DestinationEnum.values()) {
                if (dest.getEname().equals(name)) {
                    return dest.getCode();
                }
            }
            return null;
        }
    }

}
