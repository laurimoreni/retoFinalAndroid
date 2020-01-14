package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Registration extends AppCompatActivity {

    private EditText etDni, etFirstName, etLastName, etUsername, etEmail, etPassword, etPasswordRepeat, etTel;
    private String dni, firstName, lastName, username, email, password, passwordRepeat;
    private int telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

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
        try {
            if(etTel.getText().toString() != null) {
                telephone = Integer.parseInt(etTel.getText().toString());
            }
        } catch (NumberFormatException e) {
            telephone = 0;
        }

        // if data entered is valid, make the insert
        if (dataValidation()) {
            new userInsert(dni, firstName, lastName, email, password, telephone, getApplicationContext()).execute();
        }
    }

    /**
     * Check if data entered by the user is valid
     * @return
     */
    public boolean dataValidation() {
        // FALTA VALIDAR QUE EL DNI, EL EMAIL Y EL TELEFONO TENGAN VALORES VALIDOS
        if (dni.equals("")) {
            Toast.makeText(this, R.string.empty_dni, Toast.LENGTH_SHORT).show();
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
        if (password.equals("")) {
            Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordRepeat.equals("")) {
            Toast.makeText(this, R.string.empty_password2, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (telephone == 0) {
            Toast.makeText(this, R.string.empty_tel, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(passwordRepeat)) {
            Toast.makeText(this, R.string.password_confirm_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public class userInsert extends AsyncTask {

        private String dni, firstName, lastName, email, password;
        private int telephone;
        private Context mContext;

        public userInsert(String dni, String firstName, String lastName, String email, String password, int telephone, Context context){
          this.dni = dni;
          this.firstName = firstName;
          this.lastName = lastName;
          this.email = email;
          this.password = password;
          this.telephone = telephone;
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
            String query = "insert into usuarios (dni, nombre, apellido, contrasena, telefono, email, administrador) values (?, ?, ?, ?, ?, ?, ?)";
            try {
                con = DriverManager.getConnection(url, user, pass);
                ps = con.prepareStatement(query);
                ps.setString(1, dni);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, password);
                ps.setInt(5, telephone);
                ps.setString(6, email);
                ps.setInt(7, 0);
                rs = ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error al insertar el nuevo usuario en la base de datos");
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
