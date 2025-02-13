package com.es.crmInmobiliaria.dtos;

public class PropiedadDTO {
    private Long id;
    private String direccion;
    private Double precio;
    private Boolean vendida;
    private Boolean oculta;
    private String vendedor;

    public PropiedadDTO() {}

    public PropiedadDTO(Long id, String direccion, Double precio, Boolean vendida, Boolean oculta, String vendedor) {
        this.id = id;
        this.direccion = direccion;
        this.precio = precio;
        this.vendida = vendida;
        this.oculta = oculta;
        this.vendedor = vendedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
}
