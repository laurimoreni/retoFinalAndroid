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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    protected Modelo mod;

    private ArrayList<Integer> checkedTerritory = new ArrayList<Integer>();
    private ArrayList<Integer> checkedType = new ArrayList<Integer>();
    private int[] checkedExtra = {0,0,0};

    private EditText edtCapac;
    private CheckBox chkRestaurant, chkShop, chkCaravan;
    private SeekBar sbCapacity;

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
                edtCapac = view.findViewById(R.id.edtCapac);
                sbCapacity= view.findViewById(R.id.sbCapacity);
                sbCapacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        edtCapac.setText(String.valueOf(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                chkRestaurant = view.findViewById(R.id.chkRestaurant);
                chkShop = view.findViewById(R.id.chkShop);
                chkCaravan = view.findViewById(R.id.chkCaravan);
                if (checkedExtra[0] == 1) {
                    chkRestaurant.setChecked(true);
                }
                if (checkedExtra[1] == 1) {
                    chkShop.setChecked(true);
                }
                if (checkedExtra[2] == 1) {
                    chkCaravan.setChecked(true);
                }
//                Button btnOk = view.findViewById(R.id.btnOk);
//                Button btnCancel = view.findViewById(R.id.btnCancel);
                cargarFiltroTerritory(lnTerritory);
                cargarFiltroType(lnType);
                fijarCapacidad();//edtCapac);

                AlertDialog.Builder dialog = new AlertDialog.Builder( this );
                dialog.setView(view);
                dialog.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        maxCap = edtCapac.getText().toString();
                        if (chkRestaurant.isChecked()) {
                            checkedExtra[0] = 1;
                        } else {
                            checkedExtra[0] = 0;
                        }
                        if (chkShop.isChecked()) {
                            checkedExtra[1] = 1;
                        } else {
                            checkedExtra[1] = 0;
                        }
                        if (chkCaravan.isChecked()) {
                            checkedExtra[2] = 1;
                        } else {
                            checkedExtra[2] = 0;
                        }
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
                checkedTerritory.add(1);
                chkProv.setChecked(true);
            } else if (checkedTerritory.get(i) == 1) {
                    chkProv.setChecked(true);
            } else {
                chkProv.setChecked(false);
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
                checkedType.add(1);
                chkTipo.setChecked(true);
            }else if (checkedType.get(i) == 1) {
                chkTipo.setChecked(true);
            } else {
                chkTipo.setChecked(false);
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

    public void fijarCapacidad() {//final EditText edtCapac) {
        if(primeraVez) {
            edtCapac.setText("0", TextView.BufferType.EDITABLE);
            sbCapacity.setProgress(0);
//            edtCapac.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//                public void onFocusChange(View v, boolean hasFocus){
//                    if (hasFocus) {
//                        ((EditText) edtCapac).setText("");
//                    }
//                }
//            });

        } else {
            edtCapac.setText(maxCap, TextView.BufferType.EDITABLE);
            sbCapacity.setProgress(Integer.valueOf(maxCap));
        }
    }

    public void aplicarFiltros() {
        mod.getAlojFiltrados().clear();
        for (Alojamiento aloj : mod.getAlojamientos()) {
            if (filtrarTerritory(aloj) && filtrarTipo(aloj) && filtrarCapacidad(aloj) && filtrarRestaurant(aloj) && filtrarShop(aloj) && filtrarCaravan(aloj)) {
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

    public boolean filtrarRestaurant(Alojamiento aloj) {
        if (!chkRestaurant.isChecked()) {
            return true;
        } else if (chkRestaurant.isChecked() && aloj.getRestaurant() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean filtrarShop(Alojamiento aloj) {
        if (!chkShop.isChecked()) {
            return true;
        } else if (chkShop.isChecked() && aloj.getStore() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean filtrarCaravan(Alojamiento aloj) {
        if (!chkCaravan.isChecked()) {
            return true;
        } else if (chkCaravan.isChecked() && aloj.getAutocaravana() == 1) {
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
        ActivityCompat.finishAffinity(this);
        startActivity(homeIntent);
    }
}
