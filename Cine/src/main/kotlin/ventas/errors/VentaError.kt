package org.example.ventas.errors

/**
 * Clase sellada que representa los posibles errores relacionados con las ventas.
 * @property message El mensaje de error asociado con el error.
 */
sealed class VentaError(var message : String) {
    class VentaInvalida(message : String) : VentaError(message)
    class VentaStorageError(message: String) : VentaError(message)
}