package com.iisimpler.mysql2entity.util;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * 数据库操作工具类
 */
public class DBUtils {

    //数据库连接地址
    private static String URL;
    //用户名
    private static String USERNAME;
    //密码
    private static String PASSWORD;

    private static ResourceBundle rb = ResourceBundle.getBundle("jdbc");

    private DBUtils() {
    }

    //使用静态块加载驱动程序
    static {
        URL = rb.getString("jdbc.url");
        USERNAME = rb.getString("jdbc.username");
        PASSWORD = rb.getString("jdbc.password");
        try {
            Class.forName(rb.getString("jdbc.driverClassName"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //定义一个获取数据库连接的方法
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param rs 结果集
     * @param stat Statement
     * @param conn Connection
     */
    public static void close(ResultSet rs, Statement stat, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}