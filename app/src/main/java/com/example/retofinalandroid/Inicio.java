package com.example.retofinalandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.mysql.jdbc.Connection;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Inicio extends AppCompatActivity {
    ModeloDatos mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        mod = new ModeloDatos();

        new descargarBD(mod).execute();
    }

    public void pasarALogin() {
        Intent intent = new Intent(this, Login.class);
        Bundle args = new Bundle();
        args.putSerializable("modelo",(Serializable) mod);
        intent.putExtra("bundle", args);
        startActivity(intent);
        finish();
    }

    public void errorConexion() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.alerta)
                .setTitle(R.string.errorTitle)
                .setMessage(R.string.connectionError)
                .setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    private class descargarBD extends AsyncTask<Void, Void, Boolean> {
        private String url = "jdbc:mysql://188.213.5.150:3306/prueba";
        private String user = "ldmj";
        private String pass = "ladamijo";
        private ModeloDatos mod;
        private Connection con;

        public descargarBD(ModeloDatos mod) {
            this.mod = mod;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                con = (Connection) DriverManager.getConnection(url, user, pass);

                mod.setProvincias(descargarProvincias());
                mod.setAlojamientos(descargarAlojamientos());
                mod.setUsuarios(descargarUsuarios());
                mod.setReservas(descargarReservas());

            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean cargaOk) {
            if (cargaOk) {
                pasarALogin();
            } else {
                errorConexion();
            }
        }

        private ArrayList<Provincia> descargarProvincias() {
            ArrayList<Provincia> provincias = new ArrayList<Provincia>();
            ResultSet rs = null;
            Statement st = null;
            String query = "select * from provincias";
            try {
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    Provincia prov = new Provincia();
                    prov.setId(rs.getInt(1));
                    prov.setNombre(rs.getString(2));
                    provincias.add(prov);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return provincias;
        }

        private ArrayList<Alojamiento> descargarAlojamientos() {
            ArrayList<Alojamiento> alojamientos = new ArrayList<Alojamiento>();
            ResultSet rs = null;
            Statement st = null;
            String query = "select * from alojamientos";
            try {
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    Alojamiento aloj = new Alojamiento();
                    aloj.setSignatura(rs.getInt(1));
                    aloj.setDocumentname(rs.getString(2));
                    aloj.setTurismdescription(rs.getString(3));
                    aloj.setLodgingtype(rs.getString(4));
                    aloj.setAddress(rs.getString(5));
                    aloj.setPhone(rs.getString(6));
                    aloj.setTourismemail(rs.getString(7));
                    aloj.setWeb(rs.getString(8));
                    aloj.setMunicipality(rs.getString(9));
                    int territory = rs.getInt(10);
                    for (Provincia prov : mod.getProvincias()) {
                        if (prov.getId() == territory) {
                            aloj.setProvincia(prov);
                        }
                    }
                    aloj.setLatwgs84(rs.getFloat(11));
                    aloj.setLonwgs84(rs.getFloat(12));
                    aloj.setCapacity(rs.getInt(13));
                    aloj.setRestaurant(rs.getInt(14));
                    aloj.setStore(rs.getInt(15));
                    aloj.setAutocaravana(rs.getInt(16));
                    aloj.setImagen(rs.getBlob(17));
                    alojamientos.add(aloj);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return alojamientos;
        }

        private ArrayList<Usuario> descargarUsuarios() {
            ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
            ResultSet rs = null;
            Statement st = null;
            String query = "select * from usuarios";
            try {
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setDni(rs.getString(1));
                    usuario.setNombre(rs.getString(2));
                    usuario.setApellidos(rs.getString(3));
                    usuario.setContrasena(rs.getString(4));
                    usuario.setTelefono(rs.getString(5));
                    usuario.setEmail(rs.getString(6));
                    usuario.setAdministrador(rs.getInt(7));
                    usuarios.add(usuario);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return usuarios;
        }

        private ArrayList<Reserva> descargarReservas() {
            ArrayList<Reserva> reservas = new ArrayList<Reserva>();
            ResultSet rs = null;
            Statement st = null;
            String query = "select * from reservas";
            try {
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    Reserva res = new Reserva();
                    res.setId(rs.getInt(1));
                    String dni = rs.getString(2);
                    for (Usuario user : mod.getUsuarios()) {
                        if (user.getDni().equals(dni)) {
                            res.setUsuario(user);
                            break;
                        }
                    }
                    res.setFecha(rs.getDate(3));
                    int signatura = rs.getInt(4);
                    for (Alojamiento aloj : mod.getAlojamientos()) {
                        if (aloj.getSignatura() == (signatura)) {
                            res.setAlojamiento(aloj);
                            break;
                        }
                    }
                    res.setPersonas(rs.getInt(5));
                    reservas.add(res);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return reservas;
        }
    }
}
