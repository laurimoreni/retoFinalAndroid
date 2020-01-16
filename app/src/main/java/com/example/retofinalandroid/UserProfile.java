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
                Intent userIntent = new Intent(this, UserProfile.class);
                startActivity(userIntent);
                break;
            case R.id.logout:
                mod.setLoggedUser(null);
                Intent logoutIntent = new Intent(this, Login.class);
                startActivity(logoutIntent);
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
        new userDelete(user, getApplicationContext()).execute();
    }

    /**
     * Edit user
     */
    public void editUser(View v) {
        // add data to the intent and start the new activity
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
            String query = "delete from usuarios where dni = ?";
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
                        usuarios.remove(i);
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
