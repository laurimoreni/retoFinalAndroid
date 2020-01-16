package com.example.retofinalandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private Switch swDesc, swDate, swHour;
    private Modelo mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // get model data
        mod = (Modelo) getApplication();

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get fields
        swDesc = (Switch)findViewById(R.id.swDesc);
        swDate = (Switch)findViewById(R.id.swDate);
        swHour = (Switch)findViewById(R.id.swHour);

        SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String showDesc = prefe.getString("show_desc","");
        String showLoc = prefe.getString("show_loc","");
        String showType = prefe.getString("show_type","");

        if (showDesc.equals("false")) {
            swDesc.setChecked(false);
        } else {
            swDesc.setChecked(true);
        }
        if (showLoc.equals("false")) {
            swDate.setChecked(false);
        } else {
            swDate.setChecked(true);
        }
        if (showType.equals("false")) {
            swHour.setChecked(false);
        } else {
            swHour.setChecked(true);
        }

        // add listener to description switch
        swDesc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            if (swDesc.isChecked()) {
                editor.putString("show_desc", "true");
            } else {
                editor.putString("show_desc", "false");
            }
            editor.commit();
            }
        });

        // add listener to date switch
        swDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            if (swDate.isChecked()) {
                editor.putString("show_date", "true");
            } else {
                editor.putString("show_date", "false");
            }
            editor.commit();
            }
        });

        // add listener to hour switch
        swHour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            if (swHour.isChecked()) {
                editor.putString("show_hour", "true");
            } else {
                editor.putString("show_hour", "false");
            }
            editor.commit();
            }
        });
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
}
