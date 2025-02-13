package com.es.crmInmobiliaria.error.exception;

public class DataBaseException extends  RuntimeException {
    private static final String DESCRIPCION = "Internal Server Error (500)";

    public DataBaseException(String mensaje) {
        super(DESCRIPCION + ". " + mensaje);
    }
}