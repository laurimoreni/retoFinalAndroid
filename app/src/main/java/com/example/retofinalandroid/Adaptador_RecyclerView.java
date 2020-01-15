package com.example.retofinalandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador_RecyclerView extends RecyclerView.Adapter<Adaptador_RecyclerView.ViewHolder>{
    private ArrayList<Alojamiento> alojamientos;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }

    public Adaptador_RecyclerView(ArrayList<Alojamiento> aloj) {
        alojamientos = aloj;
    }

    @Override
    public Adaptador_RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View elementoAloj = inflater.inflate(R.layout.alojamiento, parent, false);

        ViewHolder vh  = new ViewHolder(elementoAloj);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Alojamiento aloj = alojamientos.get(position);
        holder.tvName.setText(aloj.getDocumentname());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return alojamientos.size();
    }
}
