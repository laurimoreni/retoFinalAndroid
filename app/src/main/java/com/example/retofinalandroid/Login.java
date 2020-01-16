package com.example.retofinalandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private Modelo mod;
    private Validations validations;
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mod = (Modelo) getApplication();
        validations = new Validations(mod, getApplicationContext());

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
        // if entered data is valid, get the user with especified email from database
        if (!email.equals("") & !password.equals("")) {
            usuario = validations.checkUserCredentials(email, validations.passwordHashing(password));
            if (usuario != null) {
                mod.setLoggedUser(usuario);
                Intent i = new Intent(this, AlojamientoRecyclerView.class );
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setIcon(R.mipmap.alerta)
            .setTitle(R.string.exitTitle)
            .setMessage(R.string.exitMessage)
            .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
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
