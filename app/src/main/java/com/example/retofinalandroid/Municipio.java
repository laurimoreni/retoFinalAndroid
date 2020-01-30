package com.example.retofinalandroid;

import java.io.Serializable;

public class Municipio implements Serializable {
    private Provincia provincia;
    private String municipio;

    public Municipio(Provincia provincia, String municipio) {
        this.provincia = provincia;
        this.municipio = municipio;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

}
