package com.example.retofinalandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.sql.Blob;
import java.util.ArrayList;

public class Adaptador_RecyclerView2 extends RecyclerView.Adapter<Adaptador_RecyclerView2.ViewHolder>{
    private ArrayList<Alojamiento> alojamientos;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvLoc;
        public TextView tvType;
        public TextView tvDesc;
        public ImageView image;
        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvLoc = (TextView) itemView.findViewById(R.id.tvLoc);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            image = (ImageView) itemView.findViewById(R.id.image);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Alojamiento aloj = alojamientos.get(position);
                // We can access the data within the views

            }
        }
    }



    public Adaptador_RecyclerView2(ArrayList<Alojamiento> aloj) {
        alojamientos = aloj;
    }

    @Override
    public Adaptador_RecyclerView2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View elementoAloj = inflater.inflate(R.layout.alojamiento, parent, false);

        ViewHolder vh  = new ViewHolder(context, elementoAloj);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Bitmap btm;
        Alojamiento aloj = alojamientos.get(position);
        holder.tvName.setText(aloj.getDocumentname());
        holder.tvLoc.setText(aloj.getMunicipality());
        holder.tvType.setText(aloj.getLodgingtype());
        holder.tvDesc.setText(aloj.getTurismdescription());
        Blob blob = aloj.getImagen();
        try {
            byte[] blobAsBytes = blob.getBytes(1, (int) blob.length());
            btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
            holder.image.setImageBitmap(btm);
        } catch (Exception e) {
            //holder.image.setImageBitmap(R.mipmap.alerta);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return alojamientos.size();
    }

}
