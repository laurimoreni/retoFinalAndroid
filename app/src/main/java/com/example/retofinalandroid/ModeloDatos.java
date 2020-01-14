package com.example.retofinalandroid;

import java.io.Serializable;
import java.util.ArrayList;

public class ModeloDatos implements Serializable {
    private ArrayList<Alojamiento> alojamientos;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Provincia> provincias;
    private ArrayList<Reserva> reservas;

    public ModeloDatos() {
        this.alojamientos = new ArrayList<Alojamiento>();
        this.usuarios = new ArrayList<Usuario>();
        this.provincias = new ArrayList<Provincia>();
        this.reservas = new ArrayList<Reserva>();
    }

    public ArrayList<Alojamiento> getAlojamientos() {
        return alojamientos;
    }

    public void setAlojamientos(ArrayList<Alojamiento> alojamientos) {
        this.alojamientos = alojamientos;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ArrayList<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(ArrayList<Provincia> provincias) {
        this.provincias = provincias;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }
}
