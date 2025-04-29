package com.alianza.Client_back.dto.errors;

public class ErrorMessages {
    public static final String CLIENT_NOT_FOUND = "Cliente no encontrado.";
    public static final String INVALID_CLIENT_DATA = "Datos del cliente inválidos.";
    public static final String CLIENT_ALREADY_EXISTS = "Ya existe un cliente con el mismo shared key.";
    public static final String INTERNAL_SERVER_ERROR = "Ocurrió un error interno en el servidor.";
    public static final String MISSING_REQUIRED_FIELDS = "Faltan campos obligatorios.";
    public static final String INVALID_DATE_RANGE = "El rango de fechas no es válido.";
    public static final String DATABASE_CONNECTION_ERROR = "Error al conectar con la base de datos.";
    public static final String EMAIL_ALREADY_REGISTERED = "El correo electrónico ya está registrado.";
    public static final String UNAUTHORIZED_ACCESS = "Acceso no autorizado.";
    public static final String BAD_REQUEST = "La solicitud no es válida.";
}
