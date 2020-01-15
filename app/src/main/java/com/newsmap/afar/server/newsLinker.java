package com.newsmap.afar.server;
import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.newsmap.afar.server.news;

//执行连接远程数据库获取新闻信息的任务
public class newsLinker{
    private Connection connection;

    //连接数据库
    public boolean connectToServer(){
        final String Hostname = "cdb-dcfaaxu2.bj.tencentcdb.com";
        final String User = "root";
        final String Password = "2019lcwrhh";
        final String Port = "10232";
        final String db = "/news_huanqiu";
        final String url = "jdbc:mysql://" + Hostname + ":" + Port + db;
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
        return true;
    }

    //获取新闻数据
    public boolean getNewsEvents(ArrayList<news>events){
        if(connection==null) {
            Log.e("TAG", "getNewsEvents: 尚未连接到数据库");
            return false;
        }

        Statement statement;
        ResultSet result;
        String queryNews = "SELECT * FROM news";

            try{
                statement=connection.createStatement();
                result=statement.executeQuery(queryNews);
                result.beforeFirst();
                while (result.next()){
                    news event = new news();
                    double lat = result.getDouble("latitude");
                    double lon = result.getDouble("longitude");
                    event.setLocation(new LatLng(lat, lon));
                    event.setTitle(result.getNString("title"));
                    event.setContent(result.getString("content"));
                    event.setUrl(result.getString("url"));
                    event.setDate(result.getString("date"));
                    event.setSource(result.getString("source"));
                    event.setCategory(result.getString("category"));
                    events.add(event);
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
