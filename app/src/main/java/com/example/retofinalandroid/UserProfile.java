package com.example.retofinalandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class UserProfile extends BaseActivity {

    private TextView tvDni, tvName, tvLastName, tvEmail, tvTel;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // get the current user
        user = mod.getLoggedUser();

        // get fields
        tvDni = (TextView) findViewById(R.id.tvDni);
        tvName = (TextView) findViewById(R.id.tvName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvEmail = (TextView) findViewById(R.id.tvLoc);
        tvTel = (TextView) findViewById(R.id.tvMore);

        // show user info
        tvDni.setText(user.getDni());
        tvName.setText(user.getNombre());
        tvLastName.setText(user.getApellidos());
        tvEmail.setText(user.getEmail());
        tvTel.setText(user.getTelefono());
    }

    /**
     * Delete user from database
     */
    public void deleteUser(View v) {
        new userDelete(user, getApplicationContext()).execute();
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
            String url = "jdbc:mysql://188.213.5.150:3306/prueba?useSSL=false";
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
