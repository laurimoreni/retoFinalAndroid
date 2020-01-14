package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        // if entered data is valid, get the user with especified email from database
        if (!email.equals("") & !password.equals("")) {
            new userLogin(email, getApplicationContext()).execute();
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

    public class userLogin extends AsyncTask {

        private String email, password;
        private Context mContext;

        public userLogin(String email, Context context){
            this.email = email;
            this.mContext = context;
        }

        @Override
        public ResultSet doInBackground(Object[] objects) {
            String url = "jdbc:mysql://188.213.5.150:3306/prueba?useSSL=false";
            String user = "ldmj";
            String pass = "ladamijo";
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String query = "select * from usuarios where email = ?";
            try {
                con = DriverManager.getConnection(url, user, pass);
                ps = con.prepareStatement(query);
                ps.setString(1, email);
                rs = ps.executeQuery();
            } catch (Exception e) {
                System.out.println("Error al insertar el nuevo usuario en la base de datos");
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            ResultSet rs = (ResultSet)result;
            if (rs != null) {
                Intent i = new Intent(mContext, AlojamientoList.class);
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.user_dont_exist, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
