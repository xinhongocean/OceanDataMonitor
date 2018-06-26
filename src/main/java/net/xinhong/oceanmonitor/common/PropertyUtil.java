package net.xinhong.oceanmonitor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Desc:properties文件获取工具类
 * Created by hafiz.zhang on 2016/9/15.
 */
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    static{
        loads();
    }
    //更改配置文件名
    public static void loads(){
//        loadProps("jdbc.properties");
//        loadProps("log4j.properties");
    }
    synchronized static public void loadProps(String confName){
        logger.info("开始加载properties文件内容.......");
        if(props==null || props.isEmpty()) {
            props = new Properties();
        }
        InputStream in = null;
        try {
//            　　　　　　　<!--第一种，通过类加载器进行获取properties文件流-->
                    in = PropertyUtil.class.getClassLoader().getResourceAsStream(confName);
//            　　　　　　  <!--第二种，通过类进行获取properties文件流-->
                    props.load(in);
        } catch (FileNotFoundException e) {
            logger.error(confName+"文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(confName+"文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    public static String getProperty(String key){
        if(null == props) {
            loads();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loads();
        }
        return props.getProperty(key, defaultValue);
    }

    public static Properties getProps() {
        return props;
    }

    public static void setProps(Properties props) {
        PropertyUtil.props = props;
    }

    public static Logger getLogger() {
        return logger;
    }
}