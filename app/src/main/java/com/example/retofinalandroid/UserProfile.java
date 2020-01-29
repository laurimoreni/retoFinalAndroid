package com.example.retofinalandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserProfile extends BaseActivity {

    private TextView tvDni, tvName, tvLastName, tvEmail, tvTel;
    private ListView lstReservas;
    private Usuario user;
    ArrayList<Reserva> userReservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // get the current user
        user = mod.getLoggedUser();

        // get fields
        tvDni = (TextView) findViewById(R.id.tvAlojRsv);
        tvName = (TextView) findViewById(R.id.tvFechEntradaRsv);
        tvLastName = (TextView) findViewById(R.id.tvFechSalidaRsv);
        tvEmail = (TextView) findViewById(R.id.tvNumPersRsv);
        tvTel = (TextView) findViewById(R.id.tvCapacity);

        // show user info
        tvDni.setText(user.getDni());
        tvName.setText(user.getNombre());
        tvLastName.setText(user.getApellidos());
        tvEmail.setText(user.getEmail());
        tvTel.setText(user.getTelefono());

        userReservations = new ArrayList<Reserva>();
        for (Reserva rsv : mod.getReservas()) {
            if (rsv.getUsuario().getDni().equals(mod.getLoggedUser().getDni())) {
                userReservations.add(rsv);
            }
        }
        Collections.sort(userReservations, new Comparator<Reserva>() {
            @Override
            public int compare(Reserva r1, Reserva r2) {
                return r2.getFechaEntrada().compareTo(r1.getFechaEntrada());
            }
        });

        LinearLayout lnReservas = findViewById(R.id.linearReservas);
        for (final Reserva rsv : userReservations) {
            View itemReserva = getLayoutInflater().inflate(R.layout.item_reserva_user, null);
            TextView txtNombre = itemReserva.findViewById(R.id.txtNomAlojRsv);
            TextView txtFecha = itemReserva.findViewById(R.id.txtFechaRsv);
            txtNombre.setText(rsv.getAlojamiento().getDocumentname());
            txtFecha.setText(rsv.getFechaEntrada().toString());
            itemReserva.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goToReservationDetails(rsv.getId());
                }
            });
            lnReservas.addView(itemReserva);
        }

    }

    private void goToReservationDetails(int id) {
        Intent intent = new Intent(this, ReservationDetails.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * Delete user from database
     */
    public void deleteUser(View v) {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.alerta)
                .setTitle(R.string.dialog_user_delete_title)
                .setMessage(R.string.dialog_user_delete)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    //DialogInterface.OnClickListener context = this;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new userDelete(user, getApplicationContext()).execute();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();

    }

    /**
     * Edit user
     */
    public void editUser(View v) {
        Intent i = new Intent(this, EditUser.class);
        startActivity(i);
    }

    public class userDelete extends AsyncTask<Void, Void, Integer> {

        private Usuario user;
        private Context mContext;

        public userDelete(Usuario user, Context context){
            this.user = user;
            this.mContext = context;
        }

        @Override
        public Integer doInBackground(Void... voids) {
            String url = "jdbc:mysql://188.213.5.150:3306/alojamientos_fac?useSSL=false";
            String dbuser = "ldmj";
            String dbpass = "ladamijo";
            Connection con = null;
            PreparedStatement ps = null;
            Integer rs = 0;
            String query = "update usuarios set activo = 'inactivo' where dni = ?";
            try {
                con = DriverManager.getConnection(url, dbuser, dbpass);
                ps = con.prepareStatement(query);
                ps.setString(1, user.getDni());
                rs = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                // remove user from users arraylist of the model
                ArrayList<Usuario> usuarios = mod.getUsuarios();
                for (int i = 0; i < usuarios.size(); i++) {
                    if (usuarios.get(i).getDni().equals(user.getDni())) {
                        usuarios.get(i).setActivo("inactivo");
                    }
                }
                mod.setUsuarios(usuarios);
                // show success message and go to Login view
                Toast.makeText(mContext, R.string.text_user_delete, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, Login.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.text_user_no_delete, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
