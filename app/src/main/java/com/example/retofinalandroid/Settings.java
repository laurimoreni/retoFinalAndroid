package com.example.retofinalandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends BaseActivity {

    private Switch swDesc, swDate, swHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

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

}
