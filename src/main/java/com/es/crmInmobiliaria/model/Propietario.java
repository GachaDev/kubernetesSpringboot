package com.es.crmInmobiliaria.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "propietarios")
public class Propietario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true)
    private String telefono;

    @Column(nullable = false)
    private String genero;

    private Boolean casado;

    private Integer n_hijos;

    @OneToOne(mappedBy = "propietario", cascade = CascadeType.DETACH)
    private Usuario usuario;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.DETACH)
    private List<Propiedad> propiedades;

    public Propietario() {}

    public Propietario(String nombre, String apellidos, String telefono, String genero, Boolean casado, Integer n_hijos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.genero = genero;
        this.casado = casado;
        this.n_hijos = n_hijos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Propiedad> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(List<Propiedad> propiedades) {
        this.propiedades = propiedades;
    }
}
