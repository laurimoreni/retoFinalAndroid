package com.example.retofinalandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    protected Modelo mod;

    private ArrayList<Integer> checkedTerritory = new ArrayList<Integer>();
    private ArrayList<Integer> checkedType = new ArrayList<Integer>();

    private boolean primeraVez = true;
    private String maxCap;

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
                onBackPressed();
                break;
            case R.id.filter:
                View view = getLayoutInflater().inflate( R.layout.filter_panel, null);
                //inicializar arrays de clicks
                LinearLayout lnTerritory = view.findViewById(R.id.panelTerritory);
                LinearLayout lnType = view.findViewById(R.id.panelType);
                final EditText edtCapac = view.findViewById(R.id.edtCapac);
                cargarFiltroTerritory(lnTerritory);
                cargarFiltroType(lnType);
                fijarCapacidad(edtCapac);

                AlertDialog.Builder dialog = new AlertDialog.Builder( this );
                dialog.setView(view);
                dialog.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        maxCap = edtCapac.getText().toString();
                        aplicarFiltros();
                    }
                });
                dialog.setNegativeButton(R.string.dialog_cancel, null);
                dialog.show();
                primeraVez = false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cargarFiltroTerritory(LinearLayout layout) {
        ArrayList<Provincia> provincias = mod.getProvincias();

        for (int i=0; i<provincias.size();i++) {
            CheckBox chkProv = new CheckBox(this);
            chkProv.setText(provincias.get(i).getNombre());
            if (primeraVez) {
                checkedTerritory.add(0);
            } else if (checkedTerritory.get(i) == 1) {
                    chkProv.setChecked(true);
            }
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

    public void cargarFiltroType(LinearLayout layout){
        ArrayList<String> tiposAlojamiento = mod.getTiposAlojamiento();

        for (int i=0; i<tiposAlojamiento.size();i++) {
            CheckBox chkTipo = new CheckBox(this);
            chkTipo.setText(tiposAlojamiento.get(i));
            if (primeraVez) {
                checkedType.add(0);
            }else if (checkedType.get(i) == 1) {
                chkTipo.setChecked(true);
            }
            chkTipo.setOnClickListener(new setCheckedType(i));

            layout.addView(chkTipo);
        }
    }

    public class setCheckedType implements View.OnClickListener {
        int pos;

        public setCheckedType(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if (checkedType.get(pos) == 0)
                checkedType.set(pos,1 );
            else
                checkedType.set(pos, 0);
        }
    }

    public void fijarCapacidad(final EditText edtCapac) {
        if(primeraVez) {
            edtCapac.setText("0", TextView.BufferType.EDITABLE);
            edtCapac.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                public void onFocusChange(View v, boolean hasFocus){
                    if (hasFocus) {
                        ((EditText) edtCapac).setText("");
                    }
                }
            });

        } else {
            edtCapac.setText(maxCap, TextView.BufferType.EDITABLE);
        }
    }

    public void aplicarFiltros() {
        mod.getAlojFiltrados().clear();
        for (Alojamiento aloj : mod.getAlojamientos()) {
            if (filtrarTerritory(aloj) && filtrarTipo(aloj) && filtrarCapacidad(aloj)) {
                mod.getAlojFiltrados().add(aloj);
            }
        }
        mod.getRvAlojamientos().getAdapter().notifyDataSetChanged();
        mod.getRvAlojamientos().smoothScrollToPosition(0);
    }

    public boolean filtrarTerritory(Alojamiento aloj) {
        for(int i=0;i<checkedTerritory.size();i++) {
            if (checkedTerritory.get(i) == 1) {
                if(aloj.getProvincia().getId() == mod.getProvincias().get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean filtrarTipo(Alojamiento aloj) {
        for(int i=0;i<checkedType.size();i++) {
            if (checkedType.get(i) == 1) {
                if(aloj.getLodgingtype().equals(mod.getTiposAlojamiento().get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean filtrarCapacidad(Alojamiento aloj) {
        int capacidad;
        if (maxCap.equals("")) {
            capacidad = 0;
        } else {
            capacidad = Integer.parseInt(maxCap);
        }
        if(aloj.getCapacity() >= capacidad) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (this.getClass().getSimpleName().equals("AlojamientoRecyclerView")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.alerta)
                    .setTitle(R.string.exitTitle)
                    .setMessage(R.string.exitMessage)
                    .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                        DialogInterface.OnClickListener context = this;
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                salirAplicacion();
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .show();
        } else {
            finish();
        }
    }

    public void salirAplicacion() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
