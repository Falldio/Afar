package com.newsmap.afar.server;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;

//执行连接远程数据库获取新闻信息的任务
public class newsLinker {
    private Connection connection;

    //连接数据库
    public boolean connectToServer(){
        final String Hostname = "cdb-dcfaaxu2.bj.tencentcdb.com";
        final String User = "root";
        final String Password = "2019lcwrhh";
        final String Port = "10232";
        final String db = "/news_huanqiu";
        final String url = "jdbc:mysql://" + Hostname + ":" + Port + db;

        new Thread() {

            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection(url, User, Password);
                    if (connection == null) {
                        Log.e("TAG", "数据库连接失败");
                    }
                } catch (
                        SQLException e) {
                    e.printStackTrace();
                } catch (
                        ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

    //获取新闻数据
    public boolean getNewsEvents(){
        if(connection==null) {
            Log.e("TAG", "getNewsEvents: 尚未连接到数据库");
        }
        String query=null;
        Statement statement=null;
        return true;
    }
}
