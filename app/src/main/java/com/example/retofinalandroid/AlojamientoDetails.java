package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlojamientoDetails extends AppCompatActivity {

    private TextView tvName, tvDesc, tvLoc, tvType, tvMore, tvPriority;
    private Button btnReservar;
    private Alojamiento alojamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alojamiento_details);

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the current alojamiento
        Intent i = getIntent();
        alojamiento = (Alojamiento)i.getSerializableExtra("alojamiento");

        // get fields
        tvName = (TextView) findViewById(R.id.tvName);
        tvDesc = (TextView) findViewById(R.id.tvLastName);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        tvType = (TextView) findViewById(R.id.tvType);
        tvMore = (TextView) findViewById(R.id.tvMore);
        btnReservar = (Button) findViewById(R.id.btnReservar);

        // show alojamiento info in the fields
        tvName.setText(alojamiento.getDocumentname());
        tvDesc.setText(alojamiento.getTurismdescription());
        tvLoc.setText(alojamiento.getProvincia().getNombre() + ", " + alojamiento.getMunicipality());
        tvType.setText(alojamiento.getLodgingtype());
        tvMore.setText("Capacidad:" + alojamiento.getCapacity());
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
                SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
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
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Delete alojamiento from database
     */
    public void reservarAlojamiento(View v) {
        int cod = alojamiento.getSignatura();
        // FALTA ENVIAR A ACTIVITY DE RESERVA
    }

}
