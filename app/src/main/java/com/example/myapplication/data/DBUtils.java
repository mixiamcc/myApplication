package com.example.myapplication.data;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * 数据库工具类：连接数据库用、获取数据库数据用
 * 相关操作数据库的方法均可写在该类
 */
public class DBUtils {

    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

    private static String user = "mcc";// 用户名

    private static String password = "123456";// 密码

    private final static String url = "jdbc:mysql://192.168.1.58:3306/mydb?useUnicode=true&characterEncodeing=UTF-8&useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个

    private Statement st = null;
    private ResultSet rs = null;
    private static Connection connection = null;

    private static Connection getConnect() {

        try {
            Class.forName(driver);// 动态加载类
            connection = (Connection) DriverManager.getConnection(url, user, password); // 尝试建立到给定数据库URL的连接
        } catch (Exception e) {
            System.out.println("连接失败");
            e.printStackTrace();
        }
        return connection;
    }

    public static HashMap<String, Object> getInfoByName(String name) {

        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection conn = getConnect();
        try {
            // mysql简单的查询语句,name为表名
            String sql = "select * from " + name;
            Log.d("sql：", sql);
            if (conn != null) {// connection不为null表示与数据库建立了连接
                Log.d("连接数据库：", "成功");

                PreparedStatement ps = conn.prepareStatement(sql);
                if (ps != null) {
                    Log.d("数据：", "成功");
                    //ps.setString(1, name);

                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null) {
                        int count = rs.getMetaData().getColumnCount();
                        Log.d("DBUtils", "列总数：" + count);
                        while (rs.next()) {
                            for (int i = 1; i <= count; i++) {
                                String field = rs.getMetaData().getColumnName(i);
                                map.put(field, rs.getString(field));
                            }
                        }
                        conn.close();
                        ps.close();
                        rs.close();
                        return map;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return null;
        }

    }

}