package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Registration extends AppCompatActivity {

    private Modelo mod;
    private Validations validations;
    private EditText etDni, etFirstName, etLastName, etEmail, etPassword, etPasswordRepeat, etTel;
    private String dni, firstName, lastName, email, password, passwordRepeat, telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mod = (Modelo) getApplication();
        validations = new Validations(mod, getApplicationContext());

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get fields
        etDni = (EditText)findViewById(R.id.etDni);
        etFirstName = (EditText)findViewById(R.id.etName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etPasswordRepeat = (EditText)findViewById(R.id.etPasswordRepeat);
        etTel = (EditText)findViewById(R.id.etTel);
    }

    /**
     * Action Bar back button action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Registration button
     * @param v
     */
    public void register(View v) {
        dni = etDni.getText().toString();
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        passwordRepeat = etPasswordRepeat.getText().toString();
        telephone = etTel.getText().toString();
        Usuario oldUser = validations.checkUserInactive(dni, email);

        // if data entered is valid, make the insert
        if (dataValidation()) {
            new userInsert(oldUser, dni, firstName, lastName, email, validations.passwordHashing(password), telephone, getApplicationContext()).execute();
        }
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
        if (password.equals("")) {
            Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordRepeat.equals("")) {
            Toast.makeText(this, R.string.empty_password2, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(passwordRepeat)) {
            Toast.makeText(this, R.string.password_confirm_error, Toast.LENGTH_SHORT).show();
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

    public class userInsert extends AsyncTask <Void, Void, Integer> {

        private String dni, firstName, lastName, email, password, telephone;
        private Usuario oldUser;
        private Context mContext;

        public userInsert(Usuario oldUser, String dni, String firstName, String lastName, String email, String password, String telephone, Context context){
            this.oldUser = oldUser;
            this.dni = dni;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.telephone = telephone;
            this.mContext = context;
        }

        @Override
        public Integer doInBackground(Void... param) {
            String url = "jdbc:mysql://188.213.5.150:3306/alojamientos_fac?useSSL=false";
            String user = "ldmj";
            String pass = "ladamijo";
            Connection con = null;
            PreparedStatement ps = null;
            Integer rs = 0;
            String query = null;
            if (oldUser != null) {
                if(oldUser.getDni().equals(dni)) {
                    query = "update usuarios set dni = ?, nombre = ?, apellido = ?, contrasena = ?, telefono = ?, email = ?, administrador = ?, activo = ? where dni = ?";
                } else if(oldUser.getEmail().equals(email)) {
                    query = "update usuarios set dni = ?, nombre = ?, apellido = ?, contrasena = ?, telefono = ?, email = ?, administrador = ?, activo = ? where email = ?";
                }
            } else {
                query = "insert into usuarios (dni, nombre, apellido, contrasena, telefono, email, administrador, activo) values (?, ?, ?, ?, ?, ?, ?, ?)";
            }
            try {
                con = DriverManager.getConnection(url, user, pass);
                ps = con.prepareStatement(query);
                ps.setString(1, dni);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, password);
                ps.setString(5, telephone);
                ps.setString(6, email);
                ps.setInt(7, 0);
                ps.setString(8, "activo");
                if (oldUser != null) {
                    if(oldUser.getDni().equals(dni)) {
                        ps.setString(9, dni);
                    } else if(oldUser.getEmail().equals(email)) {
                        ps.setString(9, email);
                    }
                }
                rs = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                // add new user to users arraylist of the model
                ArrayList<Usuario> usuarios = mod.getUsuarios();
                usuarios.add(new Usuario(dni, firstName, lastName, email, password, telephone, 0, "activo"));
                mod.setUsuarios(usuarios);
                // show success message
                Toast.makeText(mContext, R.string.new_user_success, Toast.LENGTH_SHORT).show();
                // go to login activity
                Intent i = new Intent(mContext, Login.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.new_user_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
