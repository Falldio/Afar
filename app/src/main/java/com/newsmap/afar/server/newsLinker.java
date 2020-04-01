package com.newsmap.afar.server;
import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.newsmap.afar.data.news;
import com.newsmap.afar.data.eventLayer;
import com.newsmap.afar.data.abstractNews;

//执行连接远程数据库获取新闻信息的任务
public class newsLinker{
    private Connection connection;

    //连接数据库
    public boolean connectToServer(){
        final String Hostname = "cdb-dcfaaxu2.bj.tencentcdb.com";
        final String User = "root";
        final String Password = "2019lcwrhh";
        final String Port = "10232";
        final String db = "/news_xinhua";
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
    public boolean initNews(eventLayer layer){
        if(connection==null) {
            Log.e("TAG", "initNews: 尚未连接到数据库");
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
                if(result.getInt("flag")==3) {
                    news event = new news();
                    double lat = result.getDouble("latitude");
                    double lon = result.getDouble("longitude");
                    double lat1=lat+Math.random();
                    double lon1=lon+Math.random();
                    event.setLocation(new LatLng(lat1, lon1));

                    String[] keywords=result.getString("keywords").split(",");
                    for(String keyword:keywords){
                        event.addKeyWord(keyword);
                    }
                    event.setTitle(result.getNString("title"));
                    event.setContent(result.getString("content"));
                    event.setUrl(result.getString("url"));
                    event.setDate(result.getString("date"));
                    event.setSource(result.getString("source"));
                    event.setCategory(result.getString("category"));
                    String ReNewsId=result.getString("ReNewsId");
                    if (ReNewsId!=null&&!ReNewsId.equals("无")){
                        String[]relatedId=ReNewsId.split(",");
                        event.relatedNews=new int[relatedId.length];
                        for (int i=0;i<relatedId.length;i++){
                            event.relatedNews[i]=Integer.parseInt(relatedId[i]);
                        }
                    }
                    event.setId(result.getInt("newsId"));
                    if (event.getCategory().equals("国际要闻")) {
                        layer.internationalNews.add(event);


                    }else if (event.getCategory().equals("国内新闻")){
                        layer.domesticNews.add(event);
                    }
                }
            }
            result.close();

            String queryCountries="select latitude, longitude, location, count(location) as count from news " +
                    "where category ='国际要闻' and flag=3 group by latitude, longitude, location";
            ResultSet queryResult;
            ArrayList<abstractNews> countries=new ArrayList<>();
            statement=connection.createStatement();
            queryResult=statement.executeQuery(queryCountries);
            queryResult.beforeFirst();
            while (queryResult.next()){
                abstractNews country=new abstractNews();
                country.setLocation(queryResult.getString("location"));
                country.setCount(queryResult.getInt("count"));
                double lat=queryResult.getDouble("latitude");
                double lon=queryResult.getDouble("longitude");
                country.setCoordinate(new LatLng(lat,lon));
                countries.add(country);
            }
            queryResult.close();
            layer.countries=countries;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
