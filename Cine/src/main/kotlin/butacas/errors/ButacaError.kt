package org.example.butacas.errors

/**
 * Clase que representa los posibles errores de butacas
 * @property message El mensaje de error asociado al error.
 */
sealed class ButacaError (val message : String){
    class ButacaInvalida(message: String) : ButacaError(message)
    class ButacaStorageError(message: String) : ButacaError(message)
    class ButacaNotFoundError(message: String) : ButacaError(message)
}