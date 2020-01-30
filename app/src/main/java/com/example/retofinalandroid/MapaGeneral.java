package com.example.retofinalandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.SeekBar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    LatLngBounds.Builder builder;
    SeekBar skbRadio;
    TextView txtKm;
    String km = "0";

    Location localizacion;
    double homeLat, homeLong;

    FloatingActionButton bola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_general);

        mod = (Modelo) getApplication();
        bola = findViewById(R.id.floatingActionButton);

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
            localizacion = manejador.getLastKnownLocation(proveedor);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            setMarkers();
            setCamera();
            mapReady = true;
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                    ObjectAnimator animation = ObjectAnimator.ofFloat(bola, "translationY", -128f);
                    animation.setDuration(500);
                    animation.start();
                }

                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(bola, "translationY", 0f);
                animation.setDuration(500);
                animation.start();
            }
        });
    }

    private void setMarkers() {
        builder = new LatLngBounds.Builder();
        for (Alojamiento aloj : mod.getAlojFiltrados()) {
            MarkerOptions opcionesMarcador = new MarkerOptions()
                    .position(new LatLng(aloj.getLatwgs84(), aloj.getLonwgs84()))
                    .title(aloj.getDocumentname())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            Marker marcador = mMap.addMarker(opcionesMarcador);
            marcador.setTag(aloj);
            builder.include(marcador.getPosition());
        }
    }

    private void setCamera() {
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        LatLngBounds bounds = builder.build();
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        mMap.setOnInfoWindowClickListener(this);
    }

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

    public void onMapSearch(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        view = getLayoutInflater().inflate(R.layout.on_map_search_dialog, null);
        skbRadio = view.findViewById(R.id.skbRadio);
        txtKm = view.findViewById(R.id.txtKm);
        txtKm.setText(km + " Km");
        skbRadio.setProgress(Integer.parseInt(km));
        skbRadio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    txtKm.setText(skbRadio.getProgress() + " Km");
                    km = String.valueOf(skbRadio.getProgress());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }

        );
        dialog.setView(view);
        dialog.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMap.clear();
                int radio = skbRadio.getProgress();
                setCurrentPosition(radio);
                filterByDistance(radio);
                if (mod.getAlojFiltrados().size() > 0) {
                    setMarkers();
                    setCamera();
                    mod.getRvAlojamientos().getAdapter().notifyDataSetChanged();
                } else {
                    mostrarMensaje();
                }
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
    }

    private void setCurrentPosition(int radio) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            //Permission is granted
            localizacion = manejador.getLastKnownLocation(proveedor);
            homeLat = localizacion.getLatitude();
            homeLong = localizacion.getLongitude();
            MarkerOptions opcionesMarcador = new MarkerOptions()
                    .position(new LatLng(homeLat, homeLong))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            Marker marcador = mMap.addMarker(opcionesMarcador);
            builder.include(marcador.getPosition());
        }
    }


    private void filterByDistance(int radio) {
        mod.getAlojFiltrados().clear();
        for (Alojamiento aloj : mod.getAlojamientos()) {
            double distance = calcularDistancia(aloj.getLatwgs84(), aloj.getLonwgs84());
            if (distance <= radio) {
                mod.getAlojFiltrados().add(aloj);
            }
        }
    }

    private double calcularDistancia(float alojLat, float alojLong) {
        double distancia = 0;

        distancia = Math.sqrt(Math.pow((homeLat - alojLat), 2) + Math.pow((homeLong - alojLong), 2)) * 111;
        return distancia;
    }

    private void mostrarMensaje() {
        Toast.makeText(this, "No hay alojamiento para mostrar en ese radio", Toast.LENGTH_SHORT).show();
    }
}
