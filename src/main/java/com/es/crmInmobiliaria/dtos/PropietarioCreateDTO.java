package com.es.crmInmobiliaria.dtos;

public class PropietarioCreateDTO {
    private String nombre;
    private String apellidos;
    private String telefono;
    private String genero;
    private Boolean casado;
    private Integer n_hijos;

    public PropietarioCreateDTO() {}

    public PropietarioCreateDTO(String nombre, String apellidos, String telefono, String genero, Boolean casado, Integer n_hijos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.genero = genero;
        this.casado = casado;
        this.n_hijos = n_hijos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Boolean getCasado() {
        return casado;
    }

    public void setCasado(Boolean casado) {
        this.casado = casado;
    }

    public Integer getN_hijos() {
        return n_hijos;
    }

    public void setN_hijos(Integer n_hijos) {
        this.n_hijos = n_hijos;
    }
}
