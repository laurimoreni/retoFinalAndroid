package com.example.retofinalandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class EditUser extends BaseActivity {

    private EditText etDni, etFirstName, etLastName, etEmail, etTel;
    private String dni, firstName, lastName, email, telephone;
    private Usuario currentUser;
    private Validations validations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        validations = new Validations(mod, getApplicationContext());

        // get the current currentUser
        currentUser = mod.getLoggedUser();

        // get fields
        etDni = (EditText)findViewById(R.id.etDni);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etDesc);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etTel = (EditText)findViewById(R.id.etTel);

        // show currentUser info in the fields
        etDni.setText(currentUser.getDni());
        etFirstName.setText(currentUser.getNombre());
        etLastName.setText(currentUser.getApellidos());
        etEmail.setText(currentUser.getEmail());
        etTel.setText(currentUser.getTelefono());
    }

    /**
     * Save a the currentUser data to database
     * @param v
     */
    public void saveUser(View v) {

        // get form values
        dni = etDni.getText().toString();
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        telephone = etTel.getText().toString();

        // create new currentUser object
        Usuario newUser = new Usuario(dni, firstName, lastName, email, currentUser.getContrasena(), telephone, currentUser.getAdministrador(), currentUser.getActivo());

        // if data entered is valid, make the update
        if (dataValidation()) {
            new updateUser(currentUser, newUser, getApplicationContext()).execute();
        }
    }

    /**
     * Starts the UserProfile activity
     * @param v
     */
    public void cancelUser(View v) {
        Intent i = new Intent(this, UserProfile.class);
        startActivity(i);
    }

    /**
     * Check if data entered by the currentUser is valid
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
        if (validations.checkUserExist(dni, email)) {
            Toast.makeText(this, R.string.user_exists, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Open a dialog to change the currentUser password
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
            if (validations.newPasswordValidation(currentUser.getContrasena(), oldPassword, newPassword)) {
                new updateUserPassword(currentUser, validations.passwordHashing(newPassword), getApplicationContext()).execute();
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

    /**
     * Class for updating currentUser data except password and administrator fields
     */
    public class updateUser extends AsyncTask<Void, Void, Integer> {

        private Usuario currentUser;
        private Usuario newUser;
        private Context mContext;

        public updateUser(Usuario currentUser, Usuario newUser, Context context){
            this.currentUser = currentUser;
            this.newUser = newUser;
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
            String query = "update usuarios set dni = ?, nombre = ?, apellido = ?, telefono = ?, email = ? where dni = ?";
            try {
                con = DriverManager.getConnection(url, dbuser, dbpass);
                ps = con.prepareStatement(query);
                ps.setString(1, newUser.getDni());
                ps.setString(2, newUser.getNombre());
                ps.setString(3, newUser.getApellidos());
                ps.setString(4, newUser.getTelefono());
                ps.setString(5, newUser.getEmail());
                ps.setString(6, currentUser.getDni());
                rs = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                // update info in the users arraylist of the model
                ArrayList<Usuario> usuarios = mod.getUsuarios();
                for (int i = 0; i < usuarios.size(); i++) {
                    if (usuarios.get(i).getDni().equals(currentUser.getDni())) {
                        usuarios.set(i, newUser);
                    }
                }
                mod.setUsuarios(usuarios);
                // show success message and go to currentUser profile activity
                Toast.makeText(mContext, R.string.edit_user_success, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, UserProfile.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.edit_user_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Class for updating currentUser password
     */
    public class updateUserPassword extends AsyncTask<Void, Void, Integer> {

        private Usuario user;
        private String newPassword;
        private Context mContext;

        public updateUserPassword(Usuario user, String newPassword, Context context){
            this.user = user;
            this.newPassword = newPassword;
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
            String query = "update usuarios set contrasena = ? where dni = ?";
            try {
                con = DriverManager.getConnection(url, dbuser, dbpass);
                ps = con.prepareStatement(query);
                ps.setString(1, newPassword);
                ps.setString(2, user.getDni());
                rs = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                // update info in the users arraylist of the model
                ArrayList<Usuario> usuarios = mod.getUsuarios();
                for (int i = 0; i < usuarios.size(); i++) {
                    if (usuarios.get(i).getDni().equals(user.getDni())) {
                        usuarios.get(i).setContrasena(newPassword);
                    }
                }
                mod.setUsuarios(usuarios);
                // show success message and go to currentUser profile activity
                Toast.makeText(mContext, R.string.edit_user_success, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, EditUser.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.edit_pass_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
