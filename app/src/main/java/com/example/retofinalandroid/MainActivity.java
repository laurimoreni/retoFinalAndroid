package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // cargar los datos de los archivos json en los objetos
        ArrayList<Alojamiento> alojamientos = cargarAlojamientos();
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
