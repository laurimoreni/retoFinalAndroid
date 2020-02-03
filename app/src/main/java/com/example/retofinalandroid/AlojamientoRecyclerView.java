package com.example.retofinalandroid;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.sql.Blob;
import java.util.ArrayList;

public class AlojamientoRecyclerView extends BaseActivity {
    ConstraintLayout lyLista, lyEmpty;
    SharedPreferences sharedPref;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alojamiento_recycler_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        sharedPref = getSharedPreferences("datos", Context.MODE_PRIVATE);

        lyLista = findViewById(R.id.linearLista);
        lyEmpty = findViewById(R.id.linearEmpty);
        searchView = (SearchView) findViewById(R.id.busqueda);

        // asociar searchview con searchable.xml
        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(sm.getSearchableInfo(getComponentName()));

        mod.setRvAlojamientos((RecyclerView) findViewById(R.id.rvAlojamientos));

        RecyclerView.Adapter adapter = new Adaptador_RecyclerView(mod.getAlojFiltrados());
        mod.getRvAlojamientos().setAdapter(adapter);
        mod.getRvAlojamientos().setLayoutManager(new LinearLayoutManager(this));
        mod.getRvAlojamientos().setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mod.getRvAlojamientos().addItemDecoration(itemDecoration);

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mod.getAlojFiltrados().clear();
                filtrar(query);
                mod.getRvAlojamientos().getAdapter().notifyDataSetChanged();
                mod.getRvAlojamientos().smoothScrollToPosition(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mod.getAlojFiltrados().clear();
                filtrar(newText);
                mod.getRvAlojamientos().getAdapter().notifyDataSetChanged();
                mod.getRvAlojamientos().smoothScrollToPosition(0);
                return true;
            }
        });
    }

    public void filtrar(String query) {
        for (Alojamiento aloj : mod.getAlojamientos()) {
            boolean name = aloj.getDocumentname().toLowerCase().contains(query.toLowerCase());
            boolean municipality = aloj.getMunicipality().toLowerCase().contains(query.toLowerCase());
            if (name || municipality) {
                mod.getAlojFiltrados().add(aloj);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mod.getRvAlojamientos().getAdapter().notifyDataSetChanged();
        searchView.clearFocus();
    }

    private class Adaptador_RecyclerView extends RecyclerView.Adapter<Adaptador_RecyclerView.ViewHolder> {
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        private ArrayList<Alojamiento> alojamientos;

        public Adaptador_RecyclerView(ArrayList<Alojamiento> alojamientos) {
            this.alojamientos = alojamientos;
        }

        public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
            // each data item is just a string in this case
            public TextView tvName, tvLoc, tvType, tvCap, tvDesc;
            public ImageView image, imgRestaurant, imgShop, imgCaravan;
            private Context context;

            public ViewHolder(Context context, View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tvFechEntradaRsv);
                tvLoc = (TextView) itemView.findViewById(R.id.tvNumPersRsv);
                tvType = (TextView) itemView.findViewById(R.id.tvType);
                tvCap = (TextView) itemView.findViewById(R.id.txtCapac);
                tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tvDesc.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                }
                image = (ImageView) itemView.findViewById(R.id.image);
                imgRestaurant = (ImageView) itemView.findViewById(R.id.imgRestaurant);
                imgShop = (ImageView) itemView.findViewById(R.id.imgShop);
                imgCaravan = (ImageView) itemView.findViewById(R.id.imgCaravan);
                this.context = context;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    Alojamiento aloj = alojamientos.get(position);
                    // We can access the data within the views
                    Intent intent = new Intent(context.getApplicationContext(), AlojamientoDetails.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            String showCard = sharedPref.getString("show_card", "small");
            if (showCard.equals("small")) {
                return R.layout.alojamiento_simple;
            } else {
                return R.layout.alojamiento;
            }
        }

        @Override
        public Adaptador_RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(context);
            View elementoAloj = inflater.inflate(viewType, parent, false);

            Adaptador_RecyclerView.ViewHolder vh  = new Adaptador_RecyclerView.ViewHolder(context, elementoAloj);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(Adaptador_RecyclerView.ViewHolder holder, int position) {
            if (alojamientos.size() == 0) {
                lyEmpty.setVisibility(View.VISIBLE);
                lyLista.setVisibility(View.GONE);
            } else {
                lyEmpty.setVisibility(View.GONE);
                lyLista.setVisibility(View.VISIBLE);
            }
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Bitmap btm;
            Alojamiento aloj = alojamientos.get(position);
            holder.tvName.setText(aloj.getDocumentname());
            holder.tvLoc.setText(aloj.getMunicipality());
            holder.tvType.setText(aloj.getLodgingtype());
            holder.tvCap.setText(String.valueOf(aloj.getCapacity()));
            String showDesc = sharedPref.getString("show_desc", "true");
            String desc = aloj.getTurismdescription();
            holder.tvDesc.setText(desc.substring(0, desc.indexOf(".")) + "...");
            if (showDesc.equals("true")) {
                holder.tvDesc.setVisibility(View.VISIBLE);
            } else {
                holder.tvDesc.setVisibility(View.GONE);
            }
            Blob blob = aloj.getImagen();
            try {
                byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
                btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                holder.image.setImageBitmap(btm);
            } catch (Exception e) {
                //holder.image.setImageBitmap(R.mipmap.alerta);
            }
            if (aloj.getRestaurant() == 1) {
                holder.imgRestaurant.setImageResource(R.mipmap.baseline_restaurant_black_48dp);
            } else {
                holder.imgRestaurant.setImageResource(R.mipmap.baseline_restaurant_grey_48dp);
            }
            if (aloj.getStore() == 1) {
                holder.imgShop.setImageResource(R.mipmap.baseline_shopping_cart_black_48dp);
            } else {
                holder.imgShop.setImageResource(R.mipmap.baseline_shopping_cart_grey_48dp);
            }
            if (aloj.getAutocaravana() == 1) {
                holder.imgCaravan.setImageResource(R.mipmap.baseline_rv_hookup_black_48dp);
            } else {
                holder.imgCaravan.setImageResource(R.mipmap.baseline_rv_hookup_grey_48dp);
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (alojamientos.size() == 0) {
                lyEmpty.setVisibility(View.VISIBLE);
                lyLista.setVisibility(View.GONE);
            } else {
                lyEmpty.setVisibility(View.GONE);
                lyLista.setVisibility(View.VISIBLE);
            }
            return alojamientos.size();
        }

    }

    // Mostrar el botón de filtro del menu
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.getItem(0).setVisible(true);
        return true;
    }

    public void onGeneralMapPress (View view) {
        Intent intent = new Intent(this, MapaGeneral.class);
        startActivity(intent);
    }

}

