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
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Reservar extends BaseActivity {

    private EditText etFechaEntrada, etFechaSalida, etNumPersonas;
    private Usuario user;
    private String alojCod;
    private int year1, month1, day1, year2, month2, day2;

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
            year1 = currentDate.get(Calendar.YEAR);
            month1 = currentDate.get(Calendar.MONTH);
            day1 = currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(Reservar.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, selectedyear);
                    calendar.set(Calendar.MONTH, selectedmonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedday);
                    String myFormat = "dd/MM/yy"; //Change as you need
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                    etFechaEntrada.setText(sdf.format(calendar.getTime()));
                    day1 = selectedday;
                    month1 = selectedmonth;
                    year1 = selectedyear;
                }
            }, year1, month1, day1);
            datePicker.show();
            }
        });

        // add date picker to fechaSalida field
        etFechaSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Calendar currentDate = Calendar.getInstance();
                year2 = currentDate.get(Calendar.YEAR);
                month2 = currentDate.get(Calendar.MONTH);
                day2 = currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(Reservar.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, selectedyear);
                        calendar.set(Calendar.MONTH, selectedmonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                        etFechaSalida.setText(sdf.format(calendar.getTime()));
                        day2 = selectedday;
                        month2 = selectedmonth;
                        year2 = selectedyear;
                    }
                }, year2, month2, day2);
                datePicker.show();
            }
        });
    }

    /**
     * Delete alojamiento from database
     */
    public void reservar(View v) {
        // parse fechaEntrada and fechaSalida as Date type
        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.set(year1, month1, day1);
        calendar2.set(year2, month2, day2);
        String formatedDate1 = sdf.format(calendar1.getTime());
        String formatedDate2 = sdf.format(calendar2.getTime());
        try {
            date1 = new java.sql.Date(sdf.parse(formatedDate1).getTime());
            date2 = new java.sql.Date(sdf.parse(formatedDate2).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // parse numPersonas as int
        String numPersonasString = etNumPersonas.getText().toString();
        int numPersonas = 0;
        try {
            if(numPersonasString != null)
                numPersonas = Integer.parseInt(numPersonasString);
        } catch (NumberFormatException e) {
            numPersonas = 0;
        }
        new reservaInsert(user, date1, date2, alojCod, numPersonas, getApplicationContext()).execute();
    }

    public class reservaInsert extends AsyncTask<Void, Void, Integer> {

        private Usuario user;
        private Date fechaEntrada, fechaSalida;
        private String alojCod;
        private int personas;
        private Context mContext;

        public reservaInsert(Usuario user, Date fechaEntrada, Date fechaSalida, String alojCod, int personas, Context context){
            this.user = user;
            this.fechaEntrada = fechaEntrada;
            this.fechaSalida = fechaSalida;
            this.alojCod = alojCod;
            this.personas = personas;
            this.mContext = context;
        }

        @Override
        public Integer doInBackground(Void... param) {
            String url = "jdbc:mysql://188.213.5.150:3306/alojamientos_fac?useSSL=false";
            String dbuser = "ldmj";
            String dbpass = "ladamijo";
            Connection con = null;
            PreparedStatement ps = null;
            Integer rs = 0;
            ResultSet generatedKeys = null;
            Integer primaryKey = null;
            String query = "insert into reservas (dni, fechaEntrada, fechaSalida, alojamiento, personas) values (?, ?, ?, ?, ?)";
            try {
                con = DriverManager.getConnection(url, dbuser, dbpass);
                ps = con.prepareStatement(query);
                ps.setString(1, user.getDni());
                ps.setDate(2, fechaEntrada);
                ps.setDate(3, fechaSalida);
                ps.setString(4, alojCod);
                ps.setInt(5, personas);
                rs = ps.executeUpdate();
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    primaryKey = generatedKeys.getInt(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return primaryKey;
        }

        @Override
        protected void onPostExecute(Integer primaryKey) {
            if (primaryKey != null) {
                // add new reserva to reserva arraylist of the model
                ArrayList<Reserva> reservas = mod.getReservas();
                ArrayList<Alojamiento> alojamientos = mod.getAlojamientos();
                Alojamiento selectedAloj = null;
                for (int i = 0; i < alojamientos.size(); i++) {
                    if (alojamientos.get(i).getSignatura().equals(alojCod)) {
                        selectedAloj = alojamientos.get(i);
                    }
                }
                if (selectedAloj != null) {
                    reservas.add(new Reserva(primaryKey, user, fechaEntrada, fechaSalida, selectedAloj, personas));
                    mod.setReservas(reservas);
                    // show success message
                    Toast.makeText(mContext, R.string.new_reserva_success, Toast.LENGTH_SHORT).show();
                    // go to alojamientos list activity
                    Intent i = new Intent(mContext, AlojamientoRecyclerView.class );
                    startActivity(i);
                } else {
                    Toast.makeText(mContext, R.string.new_reserva_aloj_error, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, R.string.new_reserva_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}