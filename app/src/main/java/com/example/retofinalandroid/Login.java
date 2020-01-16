package com.example.retofinalandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Modelo mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // get fields
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);

        mod = (Modelo) getApplication();

    }

    /**
     * Login button action
     * @param v
     */
    public void login(View v) {
        Usuario usuario;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // if entered data is valid, get the user with especified email from database
        if (!email.equals("") & !password.equals("")) {
            usuario = checkUserCredentials(email, passwordHashing(password));
            if (usuario != null) {
                mod.setLoggedUser(usuario);
                Intent i = new Intent(this, About.class );
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, R.string.user_dont_exist, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.login_fill_all, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts the Registration activity
     * @param v
     */
    public void goRegister(View v) {
        Intent i = new Intent(this, Registration.class );
        startActivity(i);
    }

    public Usuario checkUserCredentials(String email, String password){
        ArrayList<Usuario> usuarios = mod.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equals(email) && usuarios.get(i).getContrasena().equals(password)) {
                return usuarios.get(i);
            }
        }
        return null;
    }

    /**
     * Metodo que se ancarga de encriptar la contraseña
     * @param password Contraseña que se quiere encriptar
     * @return Retorna la contraseña encriptada
     */
    public String passwordHashing(String password){
        String generatedPassword = null;
        try {
            // Crea una instancia de MessageDigest para MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Agrega la contraseña separada en bytes para separarla
            md.update(password.getBytes());
            // Saca los bytes separados
            byte[] bytes = md.digest();
            // bytes[] almacena los bytes en formato decimal
            // Los bytes en decimal pasan a hexadecimal
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Coge los bytes separados de la contraseña en hexadecimal y los junta en un string
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.alerta)
                .setTitle(R.string.exitTitle)
                .setMessage(R.string.exitMessage)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener()
                {
                    DialogInterface.OnClickListener context = this;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }
}
