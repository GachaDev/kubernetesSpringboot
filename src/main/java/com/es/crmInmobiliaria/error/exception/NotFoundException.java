package com.es.crmInmobiliaria.error.exception;

public class NotFoundException extends RuntimeException {
    private static final String DESCRIPCION = "Not Found (404)";

    public NotFoundException(String mensaje) {
        super(DESCRIPCION + ". " + mensaje);
    }
}