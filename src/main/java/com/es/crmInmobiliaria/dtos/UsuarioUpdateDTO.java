package com.es.crmInmobiliaria.dtos;

public class UsuarioUpdateDTO {
    private String username;
    private String password;
    private String rol;
    private String id_propietario;

    public UsuarioUpdateDTO() {}

    public UsuarioUpdateDTO(String username, String password, String rol, String id_propietario) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.id_propietario = id_propietario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getId_propietario() {
        return id_propietario;
    }

    public void setId_propietario(String id_propietario) {
        this.id_propietario = id_propietario;
    }
}
