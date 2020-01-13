package com.example.retofinalandroid;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    static Connection con=null;
    public static Connection getConnection()
    {
        if (con != null) return con;
        // get db, user, pass from settings file
        return getConnection("jdbc:mysql://188.213.5.150:3306/prueba", "ldmj", "ladamijo");
    }

    private static Connection getConnection(String db_name,String user_name,String password)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(db_name, user_name, password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return con;
    }
}
