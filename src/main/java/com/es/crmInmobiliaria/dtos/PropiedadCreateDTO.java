package com.es.crmInmobiliaria.dtos;

public class PropiedadCreateDTO {
    private String direccion;
    private Double precio;
    private Boolean vendida;
    private Boolean oculta;
    private String id_propietario;

    public PropiedadCreateDTO() {}

    public PropiedadCreateDTO(String direccion, Double precio, Boolean vendida, Boolean oculta, String id_propietario) {
        this.direccion = direccion;
        this.precio = precio;
        this.vendida = vendida;
        this.oculta = oculta;
        this.id_propietario = id_propietario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
}
