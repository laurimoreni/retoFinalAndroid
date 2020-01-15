package com.example.retofinalandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class EditUser extends AppCompatActivity {

    private EditText etDni, etFirstName, etLastName, etEmail, etTel;
    private String userDni;
    private Usuario user;
    private String dni, firstName, lastName, email, telephone;
    private Modelo mod;
    private Validations validations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        // get model data
        mod = (Modelo) getApplication();
        validations = new Validations();

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
        etDni = (EditText)findViewById(R.id.etDni);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etDesc);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etTel = (EditText)findViewById(R.id.etTel);

        // show user info in the fields
        etDni.setText(user.getDni());
        etFirstName.setText(user.getNombre());
        etLastName.setText(user.getApellidos());
        etEmail.setText(user.getEmail());
        etTel.setText(user.getTelefono());
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
     * Check if data entered by the user is valid
     * @return
     */
    public boolean dataValidation() {
        if (dni.equals("")) {
            Toast.makeText(this, R.string.empty_dni, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validations.validateDNI(dni)) {
            Toast.makeText(this, R.string.incorrect_dni, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (firstName.equals("")) {
            Toast.makeText(this, R.string.empty_firstname, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lastName.equals("")) {
            Toast.makeText(this, R.string.empty_lastname, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.equals("")) {
            Toast.makeText(this, R.string.empty_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validations.validateEmail(email)) {
            Toast.makeText(this, R.string.incorrect_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (telephone.equals("")) {
            Toast.makeText(this, R.string.empty_tel, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validations.validatePhoneNumber(telephone)) {
            Toast.makeText(this, R.string.incorrect_tel, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkUserExist(dni, email)) {
            Toast.makeText(this, R.string.user_exists, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkUserExist(String dni, String email){
        ArrayList<Usuario> usuarios = mod.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getDni().equals(dni) || usuarios.get(i).getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if data entered by the user is valid
     * @return
     */
    public boolean passwordValidation(String oldPassword, String newPassword) {
        if (oldPassword.equals("")) {
            Toast.makeText(this, R.string.empty_old_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPassword.equals("")) {
            Toast.makeText(this, R.string.empty_new_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(this, R.string.same_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Save a the user data to database
     * @param v
     */
    public void saveUser(View v) {

        // get form values
        dni = etDni.getText().toString();
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        telephone = etTel.getText().toString();

        // if data entered is valid, make the update
        if (dataValidation()) {
            // FALTA ACTUALIZAR EL USUARIO EN BASE DE DATOS !!!
        }
    }

    /**
     * Starts the UserProfile activity
     * @param v
     */
    public void cancelUser(View v) {
        // add data to the intent and start the new activity
        Intent i = new Intent(this, UserProfile.class);
        i.putExtra("user_dni", userDni);
        startActivity(i);
    }

    /**
     * Open a dialog to change the user password
     * @param v
     */
    public void changePassword(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.password_dialog, null);
        dialog.setView(view);
        final EditText etOldPassword = (EditText)view.findViewById(R.id.etOldPass);
        final EditText etNewPassword = (EditText)view.findViewById(R.id.etNewPass);
        // confirm button action
        dialog.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                // get form values
                String oldPassword = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                // if data entered is valid, make the update
                if (oldPassword.equals("")) {
                    Toast.makeText(EditUser.this, R.string.empty_old_pass, Toast.LENGTH_SHORT).show();
                } else if (newPassword.equals("")) {
                    Toast.makeText(EditUser.this, R.string.empty_new_pass, Toast.LENGTH_SHORT).show();
                } else if (oldPassword.equals(newPassword)) {
                    Toast.makeText(EditUser.this, R.string.same_pass, Toast.LENGTH_SHORT).show();
                } else {
                    // FALTA ACTUALIZAR LA CONTRASEÑA EN BASE DE DATOS
                    /*AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "todolistBD", null, 1);
                    SQLiteDatabase bd = admin.getWritableDatabase();
                    ContentValues datos = new ContentValues();
                    datos.put("password", newPassword);
                    long result = bd.update("users", datos, "cod=" + userDni, null);
                    // check if user password has been updated
                    if (result != -1) {
                        Toast.makeText(EditUser.this, R.string.edit_pass_success, Toast.LENGTH_SHORT).show();
                        dialogo1.dismiss();
                    } else {
                        Toast.makeText(EditUser.this, R.string.edit_pass_error, Toast.LENGTH_SHORT).show();
                    }
                    bd.close();*/
                }
            }
        });
        // cancel button action
        dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

}
