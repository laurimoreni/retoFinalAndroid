package com.example.retofinalandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;

public class AlojamientoDetails extends BaseActivity {

    private TextView tvName, tvDesc, tvLoc, tvType, tvMore;
    private Button btnReservar, btnMapa;
    private ImageView imagen;
    private Alojamiento alojamiento;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alojamiento_details);

        // get the current alojamiento
        Bundle extras = getIntent().getExtras();
        index = extras.getInt("position");
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
     * Delete alojamiento from database
     */
    public void reservarAlojamiento(View v) {
        String cod = alojamiento.getSignatura();
        Intent i = new Intent(this, Reservar.class);
        i.putExtra("alojamiento", cod);
        startActivity(i);
    }

    public void verEnMapa(View v) {

    }

}
