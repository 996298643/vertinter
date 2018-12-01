package com.css.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {

    static{
       String driver = PropertiesUtils.getAppConf().get("db2.driver");
       try{
           Class.forName(driver);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public static Connection getConnection(){

        String url = PropertiesUtils.getAppConf().get("db2.url");

        String user = PropertiesUtils.getAppConf().get("db2.user");

        String password = PropertiesUtils.getAppConf().get("db2.password");

        Connection con = null;

        try {
            con = DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return con;
    }

    public static void execute(String sql){
        Connection con = getConnection();
        try {
            Statement state = con.createStatement();
            state.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
