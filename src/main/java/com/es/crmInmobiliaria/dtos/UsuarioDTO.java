package com.es.crmInmobiliaria.dtos;

import java.time.LocalDate;

public class UsuarioDTO {
    private Long id;
    private String username;
    private String rol;
    private LocalDate fecha_registro;

    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String username, String rol, LocalDate fecha_registro) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.fecha_registro = fecha_registro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDate getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(LocalDate fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
}
