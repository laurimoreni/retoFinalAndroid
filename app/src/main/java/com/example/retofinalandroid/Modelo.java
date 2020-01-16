package com.example.retofinalandroid;

import android.app.Application;

import java.util.ArrayList;

public class Modelo extends Application {
    private ArrayList<Alojamiento> alojamientos;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Provincia> provincias;
    private ArrayList<Reserva> reservas;
    private Usuario loggedUser;

    public ArrayList<Alojamiento> getAlojamientos() {
        return alojamientos;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Provincia> getProvincias() {
        return provincias;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setAlojamientos(ArrayList<Alojamiento> alojamientos) {
        this.alojamientos = alojamientos;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void setProvincias(ArrayList<Provincia> provincias) {
        this.provincias = provincias;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }
}
