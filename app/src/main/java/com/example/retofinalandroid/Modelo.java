package com.example.retofinalandroid;

import android.app.Application;
import android.widget.CheckBox;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class Modelo extends Application {

    private ArrayList<Alojamiento> alojamientos;
    private ArrayList<Alojamiento> alojFiltrados;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Provincia> provincias;
    private ArrayList<Reserva> reservas;
    private ArrayList<String> tiposAlojamiento;
    private Usuario loggedUser;
    private RecyclerView rvAlojamientos;
    private ArrayList<Municipio> municipios;
    private ArrayList<Integer> checkedTerritory;
    private ArrayList<Integer> checkedType;
    private int[] checkedExtra = {0,0,0};
    private String maxCap = "0";
    private String municipioFiltro = "Todos";

    public ArrayList<Alojamiento> getAlojamientos() {
        return alojamientos;
    }

    public ArrayList<Alojamiento> getAlojFiltrados() {
        return alojFiltrados;
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

    public ArrayList<String> getTiposAlojamiento() {
        return tiposAlojamiento;
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public RecyclerView getRvAlojamientos() {
        return rvAlojamientos;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public ArrayList<Integer> getCheckedTerritory() {
        return checkedTerritory;
    }

    public ArrayList<Integer> getCheckedType() {
        return checkedType;
    }

    public int[] getCheckedExtra() {
        return checkedExtra;
    }

    public String getMaxCap() {
        return maxCap;
    }

    public String getMunicipioFiltro() {
        return municipioFiltro;
    }

    public void setAlojamientos(ArrayList<Alojamiento> alojamientos) {
        this.alojamientos = alojamientos;
    }

    public void setAlojFiltrados(ArrayList<Alojamiento> alojFiltrados) {
        this.alojFiltrados = alojFiltrados;
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

    public void setTiposAlojamiento(ArrayList<String> tiposAlojamiento) {
        this.tiposAlojamiento = tiposAlojamiento;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setRvAlojamientos(RecyclerView rvAlojamientos) {
        this.rvAlojamientos = rvAlojamientos;
    }

    public void setMunicipios(ArrayList<Municipio> municipios) {
        this.municipios = municipios;
    }

    public void setCheckedTerritory(ArrayList<Integer> checkedTerritory) {
        this.checkedTerritory = checkedTerritory;
    }

    public void setCheckedType(ArrayList<Integer> checkedType) {
        this.checkedType = checkedType;
    }

    public void setCheckedExtra(int[] checkedExtra) {
        this.checkedExtra = checkedExtra;
    }

    public void setMaxCap(String maxCap) {
        this.maxCap = maxCap;
    }

    public void setMunicipioFiltro(String municipioFiltro) {
        this.municipioFiltro = municipioFiltro;
    }

}
