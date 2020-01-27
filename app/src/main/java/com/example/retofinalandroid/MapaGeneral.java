package com.example.retofinalandroid;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Blob;
import java.util.ArrayList;

public class MapaGeneral extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    Modelo mod;
    ArrayList<LatLng> posiciones;

    LocationManager manejador;
    String proveedor;

    public GoogleMap mMap;
    boolean mapReady = false;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_general);

        mod = (Modelo) getApplication();

//        posiciones = (ArrayList<LatLng>) getIntent().getSerializableExtra("posiciones");
        ArrayList<LatLng> posiciones = new ArrayList<LatLng>();

        for (Alojamiento aloj : mod.getAlojFiltrados()) {
            posiciones.add(new LatLng(aloj.getLatwgs84(), aloj.getLonwgs84()));
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaGeneral);
        mapFragment.getMapAsync(this);

        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);

        proveedor = manejador.getBestProvider(criterio, true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            //Permission is granted
            Location localizacion = manejador.getLastKnownLocation(proveedor);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            for (Alojamiento aloj : mod.getAlojFiltrados()) {
                MarkerOptions opcionesMarcador = new MarkerOptions()
                        .position(new LatLng(aloj.getLatwgs84(), aloj.getLonwgs84()))
                        .title(aloj.getDocumentname())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                Marker marcador = mMap.addMarker(opcionesMarcador);
                marcador.setTag(aloj);
                builder.include(marcador.getPosition());
            }
            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            mMap.setInfoWindowAdapter(customInfoWindow);
            LatLngBounds bounds = builder.build();
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            mMap.setOnInfoWindowClickListener(this);

            mapReady = true;
        }
    }

//    public void onMarkerClick(String nombre) {
//        Toast.makeText(this, nombre, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, AlojamientoDetails.class);
        intent.putExtra("position", mod.getAlojFiltrados().indexOf((Alojamiento) marker.getTag()));
        startActivity(intent);
    }

    public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
        private Context context;

        public CustomInfoWindowGoogleMap (Context context) {
            this.context = context;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = ((Activity)context).getLayoutInflater().inflate(R.layout.marker_info, null);

            TextView nombre = view.findViewById(R.id.txtInfoNombre);
            TextView localidad = view.findViewById(R.id.txtInfoLoc);
            ImageView imagen = view.findViewById(R.id.imgInfo);

            nombre.setText(marker.getTitle());
            Alojamiento aloj = (Alojamiento) marker.getTag();
            localidad.setText(aloj.getMunicipality());
            Blob blob = aloj.getImagen();
            try {
                byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
                Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                imagen.setImageBitmap(btm);
            } catch (Exception e) {
            }

            return view;
        }
    }
}
