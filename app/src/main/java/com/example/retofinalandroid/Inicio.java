package com.example.retofinalandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.mysql.jdbc.Connection;

import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Inicio extends AppCompatActivity {
    Modelo mod;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        mod = (Modelo) getApplication();

        new descargarBD(mod).execute();
    }

    public void pasarALogin() {
        Intent intent = new Intent(this, AlojamientoRecyclerView.class);
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
        private Modelo mod;
        private Connection con;
        private Bitmap btm;

        public descargarBD(Modelo mod) {
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
                    aloj.setSignatura(rs.getInt("signatura"));
                    aloj.setDocumentname(rs.getString("documentname"));
                    aloj.setTurismdescription(rs.getString("turismdescription"));
                    aloj.setLodgingtype(rs.getString("lodgingtype"));
                    aloj.setAddress(rs.getString("address"));
                    aloj.setPhone(rs.getString("phone"));
                    aloj.setTourismemail(rs.getString("tourismemail"));
                    aloj.setWeb(rs.getString("web"));
                    aloj.setMunicipality(rs.getString("municipality"));
                    int territory = rs.getInt("territory");
                    for (Provincia prov : mod.getProvincias()) {
                        if (prov.getId() == territory) {
                            aloj.setProvincia(prov);
                        }
                    }
                    aloj.setLatwgs84(rs.getFloat("latwgs84"));
                    aloj.setLonwgs84(rs.getFloat("lonwgs84"));
                    aloj.setCapacity(rs.getInt("capacity"));
                    aloj.setRestaurant(rs.getInt("restaurant"));
                    aloj.setStore(rs.getInt("store"));
                    aloj.setAutocaravana(rs.getInt("autocaravana"));
                    Blob blob = rs.getBlob("imagen");
                    aloj.setImagen(blob);

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
                    usuario.setDni(rs.getString("dni"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellidos(rs.getString("apellido"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setAdministrador(rs.getInt("administrador"));
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
                    res.setId(rs.getInt("id"));
                    String dni = rs.getString("dni");
                    for (Usuario user : mod.getUsuarios()) {
                        if (user.getDni().equals(dni)) {
                            res.setUsuario(user);
                            break;
                        }
                    }
                    res.setFecha(rs.getDate("fecha"));
                    int signatura = rs.getInt("alojamiento");
                    for (Alojamiento aloj : mod.getAlojamientos()) {
                        if (aloj.getSignatura() == (signatura)) {
                            res.setAlojamiento(aloj);
                            break;
                        }
                    }
                    res.setPersonas(rs.getInt("personas"));
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
