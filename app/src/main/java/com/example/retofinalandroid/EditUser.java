package com.example.retofinalandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class EditUser extends AppCompatActivity {

    private EditText etDni, etFirstName, etLastName, etEmail, etTel;
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
        user = mod.getLoggedUser();

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
     * Save a the user data to database
     * @param v
     */
    public void saveUser(View v) {

        dni = etDni.getText().toString();
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        telephone = etTel.getText().toString();

        // get form values
        Usuario newUser = new Usuario(dni, firstName, lastName, email, user.getContrasena(), telephone, user.getAdministrador());

        // if data entered is valid, make the update
        if (dataValidation()) {
            new updateUser(user, newUser, getApplicationContext()).execute();
        }
    }

    /**
     * Starts the UserProfile activity
     * @param v
     */
    public void cancelUser(View v) {
        // add data to the intent and start the new activity
        Intent i = new Intent(this, UserProfile.class);
        startActivity(i);
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
                } else if (!passwordHashing(oldPassword).equals(user.getContrasena())) {
                    Toast.makeText(EditUser.this, R.string.wrong_old_pass, Toast.LENGTH_SHORT).show();
                } else {
                    new updateUserPassword(user, passwordHashing(newPassword), getApplicationContext()).execute();
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
            // Saca los bytes separados (se almacena los bytes en formato decimal)
            byte[] bytes = md.digest();
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

    /**
     * Class for updating user data except password and administrator fields
     */
    public class updateUser extends AsyncTask<Void, Void, Integer> {

        private Usuario user;
        private Usuario newUser;
        private Context mContext;

        public updateUser(Usuario user, Usuario newUser, Context context){
            this.user = user;
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
                ps.setString(6, user.getDni());
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
                        usuarios.set(i, newUser);
                    }
                }
                mod.setUsuarios(usuarios);
                // show success message and go to user profile activity
                Toast.makeText(mContext, R.string.edit_user_success, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, UserProfile.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.edit_user_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Class for updating user password
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
                // show success message and go to user profile activity
                Toast.makeText(mContext, R.string.edit_user_success, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, EditUser.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.edit_pass_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
