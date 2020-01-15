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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alojamiento_recycler_view);

        Bundle args = getIntent().getBundleExtra("bundle");
        mod = (ModeloDatos) args.getSerializable("modelo");

        RecyclerView rvAlojamientos = (RecyclerView) findViewById(R.id.rvAlojamientos);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        Adaptador_RecyclerView adapter = new Adaptador_RecyclerView(mod.getAlojamientos());
        rvAlojamientos.setAdapter(adapter);
        rvAlojamientos.setLayoutManager(new LinearLayoutManager(this));
    }
}

