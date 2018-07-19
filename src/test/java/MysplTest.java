package com.lwx;

import java.sql.*;

public class MysplTest {
    public  MysplTest(){
    }
    private static String url="jdbc:mysql://127.0.0.1:3306/student";
    private static String name="root";
    private static String password="root";

    public static void main(String[] args){
        try {
            //1、加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2、获取数据库连接
            Connection connection= DriverManager.getConnection(url,name,password);
            //3、！！！实现数据库连接，实现增删改查Statement
            Statement statement=connection.createStatement();
            //4、执行查询语句，得到查询结果
            ResultSet resultSet=statement.executeQuery("select name from tablename1");
            //5、遍历输出查询结果，一行一行读
            while (resultSet.next()){
                System.out.println(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
