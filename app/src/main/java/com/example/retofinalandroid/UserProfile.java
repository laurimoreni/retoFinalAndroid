package com.example.retofinalandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    private TextView tvDni, tvName, tvLastName, tvEmail, tvTel;
    private String userDni;
    private Usuario user;
    private Modelo mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // get model data
        mod = (Modelo) getApplication();

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the current user
        Bundle bundle = getIntent().getExtras();
        userDni = bundle.getString("user_dni");

        ArrayList<Usuario> usuarios = mod.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getDni().equals(userDni)) {
                user = usuarios.get(i);
            }
        }

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
     * Creates the Action Bar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.userProfile:
                SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
                String userDni = prefe.getString("user_dni","");
                Intent userIntent = new Intent(this, UserProfile.class);
                userIntent.putExtra("user_dni", userDni);
                startActivity(userIntent);
                break;
            case R.id.config:
                Intent configIntent = new Intent(this, Settings.class);
                startActivity(configIntent);
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                break;
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Delete user from database
     */
    public void deleteUser(View v) {
        new userDelete(userDni, getApplicationContext()).execute();
    }

    /**
     * Edit user
     */
    public void editUser(View v) {
        // add data to the intent and start the new activity
        Intent i = new Intent(this, EditUser.class);
        i.putExtra("user_dni", userDni);
        startActivity(i);
    }

    public class userDelete extends AsyncTask {

        private String dni;
        private Context mContext;

        public userDelete(String dni, Context context){
            this.dni = dni;
            this.mContext = context;
        }

        @Override
        public Integer doInBackground(Object[] objects) {
            String url = "jdbc:mysql://188.213.5.150:3306/prueba?useSSL=false";
            String user = "ldmj";
            String pass = "ladamijo";
            Connection con = null;
            PreparedStatement ps = null;
            Integer rs = 0;
            String query = "delete from usuarios where dni = ?";
            try {
                con = DriverManager.getConnection(url, user, pass);
                ps = con.prepareStatement(query);
                ps.setString(1, dni);
                rs = ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error al borrar el usuario de la base de datos");
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result.toString().equals("1")) {
                // add new user to users arraylist of the model
                ArrayList<Usuario> usuarios = mod.getUsuarios();
                for (int i = 0; i < usuarios.size(); i++) {
                    if (usuarios.get(i).getDni().equals(dni)) {
                        usuarios.remove(i);
                    }
                }
                mod.setUsuarios(usuarios);
                // show success message
                Toast.makeText(mContext, R.string.new_user_success, Toast.LENGTH_SHORT).show();
                //
                Intent i = new Intent(mContext, Login.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.new_user_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
