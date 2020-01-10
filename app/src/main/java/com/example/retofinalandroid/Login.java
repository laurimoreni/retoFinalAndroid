package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // get fields
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
    }

    /**
     * Login button action
     * @param v
     */
    public void login(View v) {
        Usuario usuario;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        Intent i = new Intent(this, AlojamientoList.class);
        startActivity(i);

//        // if entered data is valid, get the user with with especified email from database
//        if (!etEmail.equals("") & !etPassword.equals("")) {
//
//           // FALTA COMPROBAR SI EL USUARIO/PASSWORD SON CORRECTOS !!!
//
//        } else {
//            Toast.makeText(this, R.string.new_alojamiento_fill_all, Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * Starts the Registration activity
     * @param v
     */
    public void goRegister(View v) {
        Intent i = new Intent(this, Registration.class );
        startActivity(i);
    }
}
