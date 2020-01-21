package com.example.retofinalandroid;

import java.io.Serializable;
import java.sql.Date;

public class Reserva implements Serializable {

    private int id;
    private Usuario usuario;
    private Date fechaEntrada;
    private Date fechaSalida;
    private Alojamiento alojamiento;
    private int personas;

    public Reserva() {
    }

    public Reserva(int id, Usuario usuario, Date fechaEntrada, Date fechaSalida, Alojamiento alojamiento, int personas) {
        this.id = id;
        this.usuario = usuario;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.alojamiento = alojamiento;
        this.personas = personas;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public int getPersonas() {
        return personas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }

}