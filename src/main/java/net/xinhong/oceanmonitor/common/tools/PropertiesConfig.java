package net.xinhong.oceanmonitor.common.tools;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

public class PropertiesConfig {

    private static Logger logger = Logger.getLogger(PropertiesConfig.class);
    public static Properties config = new Properties();

    static {
        InputStream apollo = null;
        try {
            String jarpath = PropertiesConfig.class.getProtectionDomain().getCodeSource().
                    getLocation().getFile();
            String jarPathUtf = null;
            try {
                jarPathUtf = URLDecoder.decode(jarpath, "UTF-8");
                String relative = new File(jarPathUtf).getParentFile().getAbsolutePath();
                apollo = new FileInputStream(new File(relative+"/apollo.properties"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {

            }
        } catch (Exception e) {

        }
        if(apollo==null){
            apollo = PropertiesConfig.class.getClassLoader().
                    getResourceAsStream("apollo.properties");
            if(apollo==null)logger.error("消息环境变量加载错误！请检查apollo.properties路径");
        }
        try {
            config.load(apollo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getValue(String name) {
        return config.getProperty(name);
    }
}
