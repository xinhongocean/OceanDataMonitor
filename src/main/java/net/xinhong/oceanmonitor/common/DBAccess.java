package net.xinhong.oceanmonitor.common;

/**
 * Created by zxf on 2018/3/26.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAccess {
    //获取数据库连接
    public Connection getConnection() {
        Connection con = null;
        try {
            //加载数据库驱动程序
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //获取数据库链接对象
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.6.1.112:1521:xhdb", "qhztj", "qhztj123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回链接对象
        return con;
    }

    //测试
    public static void main(String[] args) {
        DBAccess dbac = new DBAccess();
        if (dbac.getConnection() != null) {
            System.out.println("Get connnection success!");
        }
    }
}
