package com.example.retofinalandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;

public class AlojamientoDetails extends BaseActivity {

    private TextView tvName, tvDesc, tvLoc, tvType, tvCapacidad;
    private Button btnReservar, btnMapa;
    private ImageView imagen, imgRestaurant, imgShop, imgCaravan;
    private Alojamiento alojamiento;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alojamiento_details);

        // get the current alojamiento
        Bundle extras = getIntent().getExtras();
        index = extras.getInt("position");
        alojamiento = mod.getAlojFiltrados().get(index);

        // get fields
        tvName = findViewById(R.id.tvFechEntradaRsv);
        tvDesc = findViewById(R.id.tvDesc);
        tvLoc = findViewById(R.id.tvNumPersRsv);
        tvType = findViewById(R.id.tvType);
        tvCapacidad = findViewById(R.id.tvCapacity);
        btnReservar = findViewById(R.id.btnReservar);
        btnMapa = findViewById(R.id.btnMapa);
        imagen = findViewById(R.id.image);
        imgRestaurant = findViewById(R.id.imgRestaurant);
        imgShop = findViewById(R.id.imgShop);
        imgCaravan = findViewById(R.id.imgCaravan);

        // show alojamiento info in the fields
        tvName.setText(alojamiento.getDocumentname());
        tvDesc.setText(alojamiento.getTurismdescription());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvDesc.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        tvLoc.setText(alojamiento.getProvincia().getNombre() + ", " + alojamiento.getMunicipality());
        tvType.setText(alojamiento.getLodgingtype());
        tvCapacidad.setText(String.valueOf(alojamiento.getCapacity()));
        Blob blob = alojamiento.getImagen();
        try {
            byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
            imagen.setImageBitmap(btm);
        } catch (Exception e) {
            //holder.image.setImageBitmap(R.mipmap.alerta);
        }

        if (alojamiento.getRestaurant() == 1) {
            imgRestaurant.setImageResource(R.mipmap.baseline_restaurant_black_48dp);
        } else {
            imgRestaurant.setImageResource(R.mipmap.baseline_restaurant_grey_48dp);
        }
        if (alojamiento.getStore() == 1) {
            imgShop.setImageResource(R.mipmap.baseline_shopping_cart_black_48dp);
        } else {
            imgShop.setImageResource(R.mipmap.baseline_shopping_cart_grey_48dp);
        }
        if (alojamiento.getAutocaravana() == 1) {
            imgCaravan.setImageResource(R.mipmap.baseline_rv_hookup_black_48dp);
        } else {
            imgCaravan.setImageResource(R.mipmap.baseline_rv_hookup_grey_48dp);
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
        Intent intent = new Intent(this, MapaAlojamiento.class);
        intent.putExtra("lat", alojamiento.getLatwgs84());
        intent.putExtra("long", alojamiento.getLonwgs84());
        intent.putExtra("alojamiento", alojamiento.getDocumentname());
        startActivity(intent);
    }

}
