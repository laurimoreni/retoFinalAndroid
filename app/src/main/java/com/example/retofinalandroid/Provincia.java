package com.example.retofinalandroid;

import java.io.Serializable;

public class Provincia implements Serializable {

    private int id;
    private String nombre;

    public Provincia() {
    }

    public Provincia(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
