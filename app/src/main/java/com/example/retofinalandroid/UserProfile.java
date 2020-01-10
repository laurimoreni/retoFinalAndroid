package com.example.retofinalandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    private TextView tvDni, tvName, tvLastName, tvEmail, tvTel;
    private String userDni;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the current user
        Bundle bundle = getIntent().getExtras();
        userDni = bundle.getString("user_cod");

        // FALTA CARGAR LOS DATOS DEL USUARIO DE BASE DE DATOS
        //access to database
        /*AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "todolistBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor row = bd.rawQuery("select * from users where cod = '" + userCod + "'", null);
        if (row.moveToFirst()) {
            user = new Usuario(
                row.getInt(0),
                row.getString(1),
                row.getString(2),
                row.getString(3),
                row.getString(4),
                row.getString(5)
            );
        } else {
            Toast.makeText(this, R.string.user_dont_exist, Toast.LENGTH_SHORT).show();
        }

        bd.close();*/

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
                SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
                String userCod = prefe.getString("user_cod","");
                Intent userIntent = new Intent(this, UserProfile.class);
                userIntent.putExtra("user_cod", userCod);
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
        userDni = user.getDni();
        // FALTA BORRAR EL USUARIO DE BASE DE DATOS
    }

    /**
     * Edit user
     */
    public void editUser(View v) {
        userDni = user.getDni();
        Intent i = new Intent(this, EditUser.class );
        i.putExtra("user_cod", userDni);
        startActivity(i);
    }
}
