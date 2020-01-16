package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;

public class AlojamientoDetails extends AppCompatActivity {

    private TextView tvName, tvDesc, tvLoc, tvType, tvMore, tvPriority;
    private Button btnReservar, btnMapa;
    private ImageView imagen;
    private Alojamiento alojamiento;
    private Modelo mod;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alojamiento_details);

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the current alojamiento
        Bundle extras = getIntent().getExtras();
        index = extras.getInt("position");

        mod = (Modelo) getApplication();
        alojamiento = mod.getAlojamientos().get(index);


        // get fields
        tvName = (TextView) findViewById(R.id.tvName);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        tvType = (TextView) findViewById(R.id.tvType);
        tvMore = (TextView) findViewById(R.id.tvMore);
        btnReservar = (Button) findViewById(R.id.btnReservar);
        btnMapa = (Button) findViewById(R.id.btnMapa);
        imagen = (ImageView) findViewById(R.id.image);

        // show alojamiento info in the fields
        tvName.setText(alojamiento.getDocumentname());
        tvDesc.setText(alojamiento.getTurismdescription());
        tvLoc.setText(alojamiento.getProvincia().getNombre() + ", " + alojamiento.getMunicipality());
        tvType.setText(alojamiento.getLodgingtype());
        tvMore.setText("Capacidad:" + alojamiento.getCapacity());
        Blob blob = alojamiento.getImagen();
        try {
            byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
            imagen.setImageBitmap(btm);
        } catch (Exception e) {
            //holder.image.setImageBitmap(R.mipmap.alerta);
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
                Intent userIntent = new Intent(this, UserProfile.class);
                startActivity(userIntent);
                break;
            case R.id.logout:
                mod.setLoggedUser(null);
                Intent logoutIntent = new Intent(this, Login.class);
                startActivity(logoutIntent);
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
