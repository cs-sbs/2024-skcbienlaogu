package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306"; // 数据库URL
        String username = "root";  // 用户名
        String password = "111111"; // 密码

        try {
            // 1. 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立连接
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("连接成功!");

            // 在这里可以执行查询或更新操作

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
