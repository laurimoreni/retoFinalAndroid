package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlojamientoDetails extends AppCompatActivity {

    private TextView tvName, tvDesc, tvDate, tvHour, tvTime, tvPriority;
    private Button btnDone;
    private Alojamiento alojamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alojamiento_details);

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the current alojamiento
        Intent i = getIntent();
        alojamiento = (Alojamiento)i.getSerializableExtra("alojamiento");

        // get fields
        tvName = (TextView) findViewById(R.id.tvName);
        tvDesc = (TextView) findViewById(R.id.tvLastName);
        tvDate = (TextView) findViewById(R.id.tvEmail);
        tvHour = (TextView) findViewById(R.id.tvHour);
        tvTime = (TextView) findViewById(R.id.tvTel);
        tvPriority = (TextView) findViewById(R.id.tvPriority);
        btnDone = (Button) findViewById(R.id.btnEdit);

        // show alojamiento info in the fields
        tvName.setText(alojamiento.getName());
        tvDesc.setText(alojamiento.getDesc());
        tvDate.setText(alojamiento.getDate());
        tvHour.setText(alojamiento.getHour());
        tvTime.setText(alojamiento.getTime());
        tvPriority.setText(alojamiento.getPriority());
        if (alojamiento.getDone() == 1) {
            btnDone.setVisibility(View.INVISIBLE);
        } else {
            btnDone.setVisibility(View.VISIBLE);
        }
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
                SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
                String userCod = prefe.getString("user_cod","");
                Intent userIntent = new Intent(this, UserProfile.class);
                userIntent.putExtra("user_cod", userCod);
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
     * Delete alojamiento from database
     */
    public void deleteAlojamiento(View v) {
        int cod = alojamiento.getCod();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "todolistBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int cant = bd.delete("alojamientos", "cod=" + cod, null);
        bd.close();
        if (cant == 1) {
            Toast.makeText(this, R.string.text_alojamiento_delete, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, AlojamientoList.class );
            startActivity(i);
        } else {
            Toast.makeText(this, R.string.text_alojamiento_no_delete, Toast.LENGTH_SHORT).show();
        }
    }

}
