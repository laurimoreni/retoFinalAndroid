package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;

public class ReservationDetails extends BaseActivity {
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rervation_details);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");

        Reserva reserva = null;
        for (Reserva rsv : mod.getReservas()) {
            if (rsv.getId() == id) {
                reserva = rsv;
                break;
            }
        }

        ImageView imagen = findViewById(R.id.imagenRsv);
        TextView nombre = findViewById(R.id.tvAlojRsv);
        TextView fechaEntrada = findViewById(R.id.tvFechEntradaRsv);
        TextView fechaSalida = findViewById(R.id.tvFechSalidaRsv);
        TextView numPersonas = findViewById(R.id.tvNumPersRsv);

        Blob blob = reserva.getAlojamiento().getImagen();
        try {
            byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
            imagen.setImageBitmap(btm);
        } catch (Exception e) {
            //holder.image.setImageBitmap(R.mipmap.alerta);
        }

        nombre.setText(reserva.getAlojamiento().getDocumentname());
        fechaEntrada.setText(reserva.getFechaEntrada().toString());
        fechaSalida.setText(reserva.getFechaSalida().toString());
        numPersonas.setText(String.valueOf(reserva.getPersonas()));
    }
}
