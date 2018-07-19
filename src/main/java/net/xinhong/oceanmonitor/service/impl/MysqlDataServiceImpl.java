package net.xinhong.oceanmonitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.xinhong.oceanmonitor.common.DataCheckUtil;
import net.xinhong.oceanmonitor.common.PropertyUtil;
import net.xinhong.oceanmonitor.common.tools.NumModelTimeUtils;
import net.xinhong.oceanmonitor.dao.impl.MUnit;
import net.xinhong.oceanmonitor.service.MonitorUnitService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Autor rongxiaokun
 * @Date 2018/7/13  10:13
 */
public class MysqlDataServiceImpl implements MonitorUnitService,Serializable{
    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlDataServiceImpl.class);
    private MUnit mUnit;
    public MysqlDataServiceImpl() {
    }

    @Override
    public float check(String type) {
        float dataRate = 0;
        if(type.contains("GFS"))
            dataRate = simpleCheck(type).getDataRate();
        return dataRate;
    }

    @Override
    public MUnit simpleCheck(String type) {
        mUnit = new MUnit();
        float dataRate = 0;
        float checkNum = 0;
        if(type.contains("GFS")){
            Connection connection =null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection= DriverManager.getConnection(PropertyUtil.getProperty("GFS_mysql_url"),
                        PropertyUtil.getProperty("GFS_mysql_name"),PropertyUtil.getProperty("GFS_mysql_password"));
                //实现数据库连接，实现增删改查Statement
                Statement statement=connection.createStatement();
                //执行查询语句，得到查询结果
                String date = NumModelTimeUtils.getGFSDateTime(new DateTime(),0);
                ResultSet resultSet=statement.executeQuery("SELECT * FROM gfsdata WHERE runtime LIKE "+"'%"+date+"%'");
                //遍历输出查询结果，一行一行读
                resultSet.last();
                int dataLen = resultSet.getRow();
                float[] dataRateAndNum = NumModelTimeUtils.getGFSDataRate(dataLen, NumModelTimeUtils.getDateTime());
                mUnit.setDataNum((int) dataRateAndNum[1]);
                mUnit.setDataRate(dataRateAndNum[0]);
                if(dataRateAndNum[0] == 100){
                    return mUnit;
                }
                List<String> fileNames = new ArrayList<>();
                while (resultSet.next()){
                    String[] name = resultSet.getString("path").split("/");
                    fileNames.add(name[name.length-1]);
                }
                //gfs的具体存入逻辑方式,存入json
                DataCheckUtil.GFSDownloadCheck(mUnit.getDataNum() , fileNames , type ,mUnit.getJson() ,date);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (connection!=null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//        return new float[]{dataRate,checkNum};
        return mUnit;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public MUnit getmUnit() {
        return mUnit;
    }

    public void setmUnit(MUnit mUnit) {
        this.mUnit = mUnit;
    }
}
