package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private TextView prueba;
    private Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prueba = (TextView)findViewById(R.id.prueba);
        new Database(getApplicationContext(), prueba).execute();

        // cargar los datos de los archivos json en los objetos
        //ArrayList<Alojamiento> alojamientos = cargarAlojamientos();

        //Conexion a la base de datos
        con = DBConnection.getConnection();

        //Codigo para hacer una consulta a la base de datos
        //Hay que hacer una nueva instanvia para cada consulta (por ser AsyncTask)
        //Las vistas imgView y txtEstado son del ejemplo
        //La clase ConsultaBD tambiçén es un ejemplo
//        ConsultaBD conexionBD = new ConsultaBD(imgView, txtEstado, con);
//        conexionBD.execute(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Alojamiento> cargarAlojamientos() {
        ArrayList<Alojamiento> alojamientos = new ArrayList<Alojamiento>();
        try {
            JSONArray jarray = new JSONArray(loadJSONFromAsset("alojamientos.json"));
            for (int i = 0, size = jarray.length(); i < size; i++) {
                Alojamiento alojamiento = new Alojamiento();
                JSONObject objectInArray = jarray.getJSONObject(i);
                alojamiento.setSignatura(objectInArray.getInt("signatura"));
                alojamiento.setDocumentname(objectInArray.getString("documentname"));
                alojamiento.setTurismdescription(objectInArray.getString("turismdescription"));
                alojamiento.setLodgingtype(objectInArray.getString("lodgingtype"));
                alojamiento.setAddress(objectInArray.getString("address"));
                alojamiento.setPhone(objectInArray.getString("phone"));
                alojamiento.setTourismemail(objectInArray.getString("tourismemail"));
                alojamiento.setWeb(objectInArray.getString("web"));
                alojamiento.setMarks(objectInArray.getString("marks"));
                alojamiento.setMunicipality(objectInArray.getString("municipality"));
                alojamiento.setLatwgs84(objectInArray.getInt("latwgs84"));
                alojamiento.setLonwgs84(objectInArray.getInt("lonwgs84"));
                alojamiento.setPostalcode(objectInArray.getString("postalcode"));
                alojamiento.setCapacity(objectInArray.getInt("capacity"));
                alojamiento.setRestaurant(objectInArray.getInt("restaurant"));
                alojamiento.setStore(objectInArray.getInt("store"));
                alojamiento.setAutocaravana(objectInArray.getInt("autocaravana"));
            }
        } catch (JSONException e) {
            e.getMessage();
        }
        return alojamientos;
    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
