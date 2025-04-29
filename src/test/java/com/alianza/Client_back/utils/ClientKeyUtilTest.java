package com.alianza.Client_back.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ClientKeyUtilTest {

    @Test
    void testGenerateSharedKeySuccess() {
        String fullName = "Luis Hernández";
        String expectedKey = "lhernández";
        String actualKey = ClientKeyUtil.generateSharedKey(fullName);
        assertEquals(expectedKey, actualKey);
    }

    @Test
    void testGenerateSharedKeyWithMultipleNames() {
        String fullName = "Luis Fernando Hernández Martínez";
        String expectedKey = "lfernando";  
        String actualKey = ClientKeyUtil.generateSharedKey(fullName);
        assertEquals(expectedKey, actualKey);
    }

    @Test
    void testGenerateSharedKeyTrimsWhitespace() {
        String fullName = "   Maria   Lopez  ";
        String expectedKey = "mlopez";
        String actualKey = ClientKeyUtil.generateSharedKey(fullName);
        assertEquals(expectedKey, actualKey);
    }

    @Test
    void testGenerateSharedKeyThrowsOnNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ClientKeyUtil.generateSharedKey(null);
        });
        assertEquals("El nombre completo no puede estar vacío", exception.getMessage());
    }

    @Test
    void testGenerateSharedKeyThrowsOnBlank() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ClientKeyUtil.generateSharedKey("   ");
        });
        assertEquals("El nombre completo no puede estar vacío", exception.getMessage());
    }

    @Test
    void testGenerateSharedKeyThrowsOnOnlyFirstName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ClientKeyUtil.generateSharedKey("Carlos");
        });
        assertEquals("Debe incluir al menos nombre y apellido", exception.getMessage());
    }
}
