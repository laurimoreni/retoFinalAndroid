package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AlojamientoList extends AppCompatActivity {

    private ArrayList<Alojamiento> alojamientoList;
    private ArrayList<Provincia> provinciasList;
    private ListView list;
    private int selectView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alojamiento_list);

        //set types of alojamiento spinner options
        ArrayAdapter<String> viewsAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.alojamientos_types));
        final Spinner alojamientoView = (Spinner)findViewById(R.id.alojamientoView);
        alojamientoView.setAdapter(viewsAdapter);

        // load alojamientos into ArrayList
        provinciasList = cargarProvincias();
        alojamientoList = viewAllAlojamientos();

        // add alojamientoList to the ListView
        AlojamientoAdapter adaptador = new AlojamientoAdapter(this);
        list = (ListView) findViewById(R.id.alojamientoList);
        list.setAdapter(adaptador);

        // add listener to ListView items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                viewDetails(alojamientoList.get(i));
            }
        });

        // Update listView when alojamientoView spinner item is selected
        alojamientoView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectView = position;
                switch(position) {
                    case 0:
                        alojamientoList = viewAllAlojamientos();
                        break;
                    case 1:
                        alojamientoList = viewRurales();
                        break;
                    case 2:
                        alojamientoList = viewAlbergues();
                        break;
                    case 3:
                        alojamientoList = viewCampings();
                        break;
                }
                AlojamientoAdapter adaptador = new AlojamientoAdapter(AlojamientoList.this);
                list = (ListView) findViewById(R.id.alojamientoList);
                list.setAdapter(adaptador);
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //alojamientoList = new ArrayList<Alojamiento>();

        alojamientoList = viewAllAlojamientos();

        // FALTA CARGAR TODOS LOS ALOJAMIENTOS EN ARRAYLIST !!
        // get all alojamientos from database and add to an ArrayList of Alojamiento object
        /*AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "todolistBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor row = bd.rawQuery("select * from alojamientos", null);
        if (row.moveToFirst()) {
            while (!row.isAfterLast()) {
                alojamientoList.add(new Alojamiento(
                        row.getInt(0),
                        row.getString(1),
                        row.getString(2),
                        row.getString(3),
                        row.getString(4),
                        row.getString(5),
                        row.getString(6),
                        row.getInt(7)
                ));
                row.moveToNext();
            }
        } else {
            Toast.makeText(this, R.string.no_alojamientos, Toast.LENGTH_SHORT).show();
        }
        bd.close();*/

        // add alojamiento to the ListView
        AlojamientoAdapter adaptador = new AlojamientoAdapter(this);
        list = (ListView) findViewById(R.id.alojamientoList);
        list.setAdapter(adaptador);

        // add listener to ListView items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                viewDetails(alojamientoList.get(i));
            }
        });

    }

    public ArrayList<Alojamiento> viewAllAlojamientos() {
        //ArrayList<Alojamiento> alojamientoList = new ArrayList<Alojamiento>();

        alojamientoList = cargarAlojamientos();

        // FALTA CARGAR TODOS LOS ALOJAMIENTOS EN ARRAYLIST !!
        // get all alojamientos from database and add to an ArrayList of Alojamiento object
       /* AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "todolistBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor row = bd.rawQuery("select * from alojamientos", null);
        if (row.moveToFirst()) {
            while (!row.isAfterLast()) {
                alojamientoList.add(new Alojamiento(
                        row.getInt(0),
                        row.getString(1),
                        row.getString(2),
                        row.getString(3),
                        row.getString(4),
                        row.getString(5),
                        row.getString(6),
                        row.getInt(7)
                ));
                row.moveToNext();
            }
        }
        bd.close();*/
        return alojamientoList;
    }

    public ArrayList<Alojamiento> viewRurales() {
        ArrayList<Alojamiento> alojamientoList = new ArrayList<Alojamiento>();
        // FALTA CARGAR TODOS LOS ALOJAMIENTOS EN ARRAYLIST !!
        // get all alojamientos from database and add to an ArrayList of Alojamiento object
        /*AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "todolistBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor row = bd.rawQuery("select * from alojamientos where done = '" + 0 + "'", null);
        ArrayList<Alojamiento> alojamientoList = new ArrayList<Alojamiento>();
        if (row.moveToFirst()) {
            while (!row.isAfterLast()) {
                alojamientoList.add(new Alojamiento(
                        row.getInt(0),
                        row.getString(1),
                        row.getString(2),
                        row.getString(3),
                        row.getString(4),
                        row.getString(5),
                        row.getString(6),
                        row.getInt(7)
                ));
                row.moveToNext();
            }
        }
        bd.close();*/
        return alojamientoList;
    }

    public ArrayList<Alojamiento> viewAlbergues() {
        ArrayList<Alojamiento> alojamientoList = new ArrayList<Alojamiento>();
        // FALTA CARGAR TODOS LOS ALOJAMIENTOS EN ARRAYLIST !!
        return alojamientoList;
    }

    public ArrayList<Alojamiento> viewCampings() {
        ArrayList<Alojamiento> alojamientoList = new ArrayList<Alojamiento>();
        // FALTA CARGAR TODOS LOS ALOJAMIENTOS EN ARRAYLIST !!
        return alojamientoList;
    }

    /**
     * Alojamiento Adapter
     */
    class AlojamientoAdapter extends ArrayAdapter<Alojamiento> {
        AppCompatActivity appCompatActivity;

        AlojamientoAdapter(AppCompatActivity context) {
            super(context, R.layout.alojamiento, alojamientoList);
            appCompatActivity = context;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.alojamiento, null);

            // get fields
            TextView name = (TextView) item.findViewById(R.id.tvName);
            TextView desc = (TextView) item.findViewById(R.id.tvDesc);
            TextView loc = (TextView) item.findViewById(R.id.tvLoc);
            TextView type = (TextView) item.findViewById(R.id.tvType);

            SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
            String showDesc = prefe.getString("show_desc","");
            String showLoc = prefe.getString("show_loc","");
            String showType = prefe.getString("show_type","");

            // show name
            name.setText(alojamientoList.get(position).getDocumentname());

            // show description
            if (showDesc.equals("false")) {
                desc.setVisibility(View.GONE);
            } else {
                desc.setVisibility(View.VISIBLE);
                desc.setText(alojamientoList.get(position).getTurismdescription());
            }

            // show loc
            if (showLoc.equals("false")) {
                loc.setVisibility(View.GONE);
            } else {
                loc.setVisibility(View.VISIBLE);
                loc.setText(alojamientoList.get(position).getProvincia().getNombre() + ", " + alojamientoList.get(position).getMunicipality());
            }

            // show type
            if (showType.equals("false")) {
                type.setVisibility(View.GONE);
            } else {
                type.setVisibility(View.VISIBLE);
                type.setText(alojamientoList.get(position).getLodgingtype());
            }

            return (item);
        }
    }

    /**
     * Creates the Action Bar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.userProfile:
                SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
                String userCod = prefe.getString("user_cod","");
                Intent userIntent = new Intent(this, UserProfile.class);
                userIntent.putExtra("user_cod", userCod);
                startActivity(userIntent);
                break;
            case R.id.config:
                Intent configIntent = new Intent(this, Settings.class);
                startActivity(configIntent);
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Starts the AlojamientoDetails activity
     */
    public void viewDetails(Alojamiento alojamiento) {
        Intent i = new Intent(this, AlojamientoDetails.class);
        i.putExtra("alojamiento", alojamiento);
        startActivity(i);
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

    public ArrayList<Alojamiento> cargarAlojamientos() {
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
                //alojamiento.setMarks(objectInArray.getString("marks"));
                // falta la provincia
                // falta la imagen
                int idProvincia = objectInArray.getJSONObject("provincia").getInt("id");
                alojamiento.setProvincia(buscarProvincia(idProvincia));
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

    Provincia buscarProvincia(int id) {
        for(Provincia provincia : provinciasList) {
            if(provincia.getId() == id) {
                return provincia;
            }
        }
        return null;
    }
}
