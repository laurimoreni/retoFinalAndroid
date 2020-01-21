package com.example.retofinalandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    protected Modelo mod;

    private ArrayList<Integer> checkedTerritory = new ArrayList<Integer>();
    private ArrayList<String> checkedType = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get model data
        mod = (Modelo) getApplication();

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            case R.id.filter:
                View view = getLayoutInflater().inflate( R.layout.filter_panel, null);
                //inicializar arrays de clicks
                checkedTerritory.clear();
                LinearLayout lnTerritory = (LinearLayout) view.findViewById(R.id.panelTerritory);
                cargarOpcionesFiltro(lnTerritory);

                AlertDialog.Builder dialog = new AlertDialog.Builder( this );
                dialog.setView(view);
                dialog.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aplicarFiltros();
                    }
                });
                dialog.setNegativeButton(R.string.dialog_cancel, null);
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void aplicarFiltros() {
        Toast.makeText(this, "Filtros aplicados", Toast.LENGTH_SHORT).show();
    }

    public void cargarOpcionesFiltro(LinearLayout layout) {
        ArrayList<Provincia> provincias = mod.getProvincias();

        for (int i=0; i<provincias.size();i++) {
            CheckBox chkProv = new CheckBox(this);
            chkProv.setText(provincias.get(i).getNombre());
            checkedTerritory.add(0);
            chkProv.setOnClickListener(new setCheckedTerritory(i));

            layout.addView(chkProv);
        }
    }

    public class setCheckedTerritory implements View.OnClickListener {
        int pos;

        public setCheckedTerritory(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if (checkedTerritory.get(pos) == 0)
                checkedTerritory.set(pos,1 );
            else
                checkedTerritory.set(pos, 0);
        }
    }

}
