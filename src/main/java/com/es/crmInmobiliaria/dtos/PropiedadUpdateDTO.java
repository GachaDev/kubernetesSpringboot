package com.es.crmInmobiliaria.dtos;

public class PropiedadUpdateDTO {
    private Double precio;
    private Boolean vendida;
    private Boolean oculta;
    private String id_propietario;
    private String id_usuario;

    public PropiedadUpdateDTO() {}

    public PropiedadUpdateDTO(Double precio, Boolean vendida, Boolean oculta, String id_propietario, String id_usuario) {
        this.precio = precio;
        this.vendida = vendida;
        this.oculta = oculta;
        this.id_propietario = id_propietario;
        this.id_usuario = id_usuario;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Boolean getVendida() {
        return vendida;
    }

    public void setVendida(Boolean vendida) {
        this.vendida = vendida;
    }

    public Boolean getOculta() {
        return oculta;
    }

    public void setOculta(Boolean oculta) {
        this.oculta = oculta;
    }

    public String getId_propietario() {
        return id_propietario;
    }

    public void setId_propietario(String id_propietario) {
        this.id_propietario = id_propietario;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
