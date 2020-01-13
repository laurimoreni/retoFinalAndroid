package com.example.retofinalandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConsultaBD extends AsyncTask <Integer, Void, Bitmap> {
    private ImageView imgView;
    private TextView txtEstado;
    private String mensaje = "";
    private Connection con;

    public ConsultaBD(ImageView imgView, TextView txtEstado, Connection con) {
        this.imgView = imgView;
        this.txtEstado = txtEstado;
        this.con = con;
    }

    @Override
    protected Bitmap doInBackground(Integer... index) {
        Bitmap btm = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            // "jdbc:mysql://IP:PUERTO/DB", "USER", "PASSWORD");
            // Si estás utilizando el emulador de android y tenes el mysql en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
//            Connection conn = DriverManager.getConnection("jdbc:mysql://188.213.5.150:3306/prueba", "ldmj", "ladamijo");
            //En el stsql se puede agregar cualquier consulta SQL deseada.
            con = DBConnection.getConnection();
            String stsql = "Select imagen from alojamientos where signatura = " + index[0];
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(stsql);
            rs.next();
            Blob blob = rs.getBlob(1);
            byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
            btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);
        } catch (Exception se) {
            mensaje = se.toString();
        }
        return btm;
    }

    @Override
    protected void onProgressUpdate(Void... param) {

    }

    @Override
    protected void onCancelled() {
        txtEstado.setText(mensaje);
    }

    @Override
    protected void onPostExecute(Bitmap btm) {
        imgView.setImageBitmap(btm);
    }
}
