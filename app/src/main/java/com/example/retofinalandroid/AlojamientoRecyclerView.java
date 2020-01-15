package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class AlojamientoRecyclerView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ModeloDatos mod;
    private Modelo appMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alojamiento_recycler_view);

//        Bundle args = getIntent().getBundleExtra("bundle");
//        mod = (ModeloDatos) args.getSerializable("modelo");

        appMod = (Modelo) getApplication();

        RecyclerView rvAlojamientos = (RecyclerView) findViewById(R.id.rvAlojamientos);

        Adaptador_RecyclerView adapter = new Adaptador_RecyclerView(appMod.getAlojamientos());
        rvAlojamientos.setAdapter(adapter);
        rvAlojamientos.setLayoutManager(new LinearLayoutManager(this));
    }
}

