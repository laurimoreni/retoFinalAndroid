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

import java.util.ArrayList;

public class AlojamientoList extends AppCompatActivity {

    private ArrayList<Alojamiento> alojamientoList;
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
        alojamientoList = new ArrayList<Alojamiento>();
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

        alojamientoList = new ArrayList<Alojamiento>();

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
        ArrayList<Alojamiento> alojamientoList = new ArrayList<Alojamiento>();
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
            TextView date = (TextView) item.findViewById(R.id.tvEmail);
            TextView hour = (TextView) item.findViewById(R.id.tvHour);

            SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
            String showDesc = prefe.getString("show_desc","");
            String showDate = prefe.getString("show_date","");
            String showHour = prefe.getString("show_hour","");

            // show name
            name.setText(alojamientoList.get(position).getDocumentname());

            // show description
            if (showDesc.equals("false")) {
                desc.setVisibility(View.GONE);
            } else {
                desc.setVisibility(View.VISIBLE);
                desc.setText(alojamientoList.get(position).getTurismdescription());
            }

            // show date
            if (showDate.equals("false")) {
                date.setVisibility(View.GONE);
            } else {
                date.setVisibility(View.VISIBLE);
                date.setText(alojamientoList.get(position).getDate());
            }

            // show hour
            if (showHour.equals("false")) {
                hour.setVisibility(View.GONE);
            } else {
                hour.setVisibility(View.VISIBLE);
                hour.setText(alojamientoList.get(position).getHour());
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
}
