package com.es.crmInmobiliaria.model;

import jakarta.persistence.*;

@Entity
@Table(name = "propiedades")
public class Propiedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_propietario", nullable = false)
    private Propietario propietario;

    @Column(nullable = false, unique = true)
    private String direccion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Boolean vendida;

    @Column(nullable = false)
    private Boolean oculta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario vendedor;

    public Propiedad() {}

    public Propiedad(Propietario propietario, String direccion, Double precio, Boolean vendida, Boolean oculta, Usuario vendedor) {
        this.propietario = propietario;
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

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
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

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }
}
