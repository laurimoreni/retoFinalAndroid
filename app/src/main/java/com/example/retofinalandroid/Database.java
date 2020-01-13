package com.example.retofinalandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database extends AsyncTask {

    private Context mContext;
    private TextView tv;

    public Database(Context context, TextView tv){
        this.mContext = context;
        this.tv = tv;
        tv.setText(doInBackground(null));
    }

    @Override
    public String doInBackground(Object[] objects) {
        String url = "jdbc:mysql://188.213.5.150:3306/prueba?serverTimezone=UTC";
        String user = "ldmj";
        String pass = "ladamijo";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String count = "Sin resultados";
        try {
            con = DriverManager.getConnection(url, user, pass);
            ps = con.prepareStatement("select count(*) from alojamientos");
            rs = ps.executeQuery();
            while(rs.next()){
                count = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
