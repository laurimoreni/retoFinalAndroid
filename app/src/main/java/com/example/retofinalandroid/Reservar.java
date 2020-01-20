package com.example.retofinalandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Reservar extends BaseActivity {

    private EditText etFechaEntrada, etFechaSalida, etNumPersonas;
    private Usuario user;
    private String alojCod;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservar);

        // get the current user
        user = mod.getLoggedUser();

        // get alojamiento code from intent parameter
        Bundle bundle = getIntent().getExtras();
        alojCod = bundle.getString("alojamiento");

        // get fields
        etFechaEntrada = (EditText)findViewById(R.id.etFechaEntrada);
        etFechaSalida = (EditText)findViewById(R.id.etFechaSalida);
        etNumPersonas = (EditText)findViewById(R.id.etNumPersonas);

        // add date picker to fechaEntrada field
        etFechaEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Calendar currentDate = Calendar.getInstance();
            year = currentDate.get(Calendar.YEAR);
            month = currentDate.get(Calendar.MONTH);
            day = currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(Reservar.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, selectedyear);
                    calendar.set(Calendar.MONTH, selectedmonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedday);
                    String myFormat = "dd/MM/yy"; //Change as you need
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                    etFechaEntrada.setText(sdf.format(calendar.getTime()));
                    day = selectedday;
                    month = selectedmonth;
                    year = selectedyear;
                }
            }, year, month, day);
            datePicker.show();
            }
        });

        // add date picker to fechaSalida field
        etFechaSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Calendar currentDate = Calendar.getInstance();
                year = currentDate.get(Calendar.YEAR);
                month = currentDate.get(Calendar.MONTH);
                day = currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(Reservar.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, selectedyear);
                        calendar.set(Calendar.MONTH, selectedmonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                        etFechaSalida.setText(sdf.format(calendar.getTime()));
                        day = selectedday;
                        month = selectedmonth;
                        year = selectedyear;
                    }
                }, year, month, day);
                datePicker.show();
            }
        });
    }

    /**
     * Delete alojamiento from database
     */
    public void reservar(View v) {
        new reservaInsert(user.getDni(), etFechaEntrada.getText(), etFechaSalida.getText(), alojCod, etNumPersonas.getText(), getApplicationContext()).execute();
    }

    public class reservaInsert extends AsyncTask<Void, Void, Integer> {

        private String dni, alojamiento;
        private String fechaEntrada, fechaSalida;
        private int personas;
        private Context mContext;

        public reservaInsert(String dni, String fechaEntrada, String fechaSalida, String alojamiento, int personas, Context context){
            this.dni = dni;
            this.fechaEntrada = fechaEntrada;
            this.fechaSalida = fechaSalida;
            this.alojamiento = alojamiento;
            this.personas = personas;
            this.mContext = context;
        }

        @Override
        public Integer doInBackground(Void... param) {
            String url = "jdbc:mysql://188.213.5.150:3306/prueba?useSSL=false";
            String user = "ldmj";
            String pass = "ladamijo";
            Connection con = null;
            PreparedStatement ps = null;
            Integer rs = 0;
            String query = "insert into usuarios (dni, fechaEntrada, fechaSalida, alojamiento, personas) values (?, ?, ?, ?, ?)";
            try {
                con = DriverManager.getConnection(url, user, pass);
                ps = con.prepareStatement(query);
                ps.setString(1, dni);
                ps.setString(2, fechaEntrada);
                ps.setString(3, fechaSalida);
                ps.setString(4, alojamiento);
                ps.setInt(5, personas);
                rs = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(Integer result) {
            /*if (result == 1) {
                // add new user to users arraylist of the model
                ArrayList<Usuario> usuarios = mod.getUsuarios();
                usuarios.add(new Usuario(dni, fechaEntrada, fechaSalida, alojamiento, personas));
                mod.setUsuarios(usuarios);
                // show success message
                Toast.makeText(mContext, R.string.new_user_success, Toast.LENGTH_SHORT).show();
                // go to login activity
                Intent i = new Intent(mContext, Login.class );
                startActivity(i);
            } else {
                Toast.makeText(mContext, R.string.new_user_error, Toast.LENGTH_SHORT).show();
            }*/
        }
    }

}
