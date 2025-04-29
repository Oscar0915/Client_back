package com.alianza.Client_back.utils;

public class ClientKeyUtil {

    public static String generateSharedKey(String fullName) {

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("El nombre completo no puede estar vacío");
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Debe incluir al menos nombre y apellido");
        }

        
        String firstNameInitial = parts[0].substring(0, 1).toLowerCase();
        String lastName = parts[1].toLowerCase();

        return firstNameInitial + lastName;
    }
}
