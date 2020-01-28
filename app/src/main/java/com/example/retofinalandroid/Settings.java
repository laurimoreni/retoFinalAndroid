package com.example.retofinalandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends BaseActivity {

    private Switch swDesc, swCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // get fields
        swDesc = (Switch)findViewById(R.id.swDesc);
        swCard = (Switch)findViewById(R.id.swCard);

        SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String showDesc = prefe.getString("show_desc","");
        String showCard = prefe.getString("show_card", "small");

        if (showDesc.equals("false")) {
            swDesc.setChecked(false);
        } else {
            swDesc.setChecked(true);
        }
        if (showCard.equals("small")) {
            swCard.setChecked(false);
        } else {
            swCard.setChecked(true);
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
            mod.getRvAlojamientos().getAdapter().notifyDataSetChanged();
            }
        });

        swCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                if (swCard.isChecked()) {
                    editor.putString("show_card", "big");
                } else {
                    editor.putString("show_card", "small");
                }
                editor.commit();
                mod.getRvAlojamientos().getAdapter().notifyDataSetChanged();
            }
        });
    }

}
