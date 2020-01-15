package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonReader extends AppCompatActivity {

    protected void ejemplo() {
        // ejemplo de uso
        ArrayList<Provincia> provinciasList = cargarProvincias();
        ArrayList<Alojamiento> alojamientoList = cargarAlojamientos(provinciasList);
    }

    public ArrayList<Provincia> cargarProvincias() {
        ArrayList<Provincia> provincias = new ArrayList<Provincia>();
        try {
            JSONArray jarray = new JSONArray(loadJSONFromAsset("provincias.json"));
            for (int i = 0; i < jarray.length(); i++) {
                Provincia provincia = new Provincia();
                JSONObject objectInArray = jarray.getJSONObject(i);
                provincia.setId(objectInArray.getInt("id"));
                provincia.setNombre(objectInArray.getString("nombre"));
                provincias.add(provincia);
            }
        } catch (JSONException e) {
            e.getMessage();
        }
        return provincias;
    }

    public ArrayList<Alojamiento> cargarAlojamientos(ArrayList<Provincia> provinciasList) {
        ArrayList<Alojamiento> alojamientos = new ArrayList<Alojamiento>();
        try {
            JSONArray jarray = new JSONArray(loadJSONFromAsset("alojamientos1.json"));
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
                int idProvincia = objectInArray.getJSONObject("provincia").getInt("id");
                alojamiento.setProvincia(buscarProvincia(idProvincia, provinciasList));
                alojamiento.setMunicipality(objectInArray.getString("municipality"));
                alojamiento.setLatwgs84(objectInArray.getInt("latwgs84"));
                alojamiento.setLonwgs84(objectInArray.getInt("lonwgs84"));
                alojamiento.setPostalcode(objectInArray.getString("postalcode"));
                alojamiento.setCapacity(objectInArray.getInt("capacity"));
                alojamiento.setRestaurant(objectInArray.getInt("restaurant"));
                alojamiento.setStore(objectInArray.getInt("store"));
                alojamiento.setAutocaravana(objectInArray.getInt("autocaravana"));
                alojamientos.add(alojamiento);
            }
        } catch (JSONException e) {
            System.out.println("error: " + e.getMessage());
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

    Provincia buscarProvincia(int id, ArrayList<Provincia> provinciasList) {
        for(Provincia provincia : provinciasList) {
            if(provincia.getId() == id) {
                return provincia;
            }
        }
        return null;
    }

}
