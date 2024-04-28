package cuenta.errors

/**
 * Representa un error personalizado para el módulo de Cuenta.
 *
 * Esta clase sellada contiene tres subclases:
 * - [CuentaInvalidaError]
 * - [CuentaStorageError]
 * - [CuentaNotFoundError]
 *
 * Cada subclase representa un escenario de error específico dentro del módulo de Cuenta.
 */
sealed class CuentaError (val message : String) {
    class CuentaInvalida(message: String) : CuentaError(message)
    class CuentaStorageError(message: String) : CuentaError(message)
    class CuentaNotFoundError(message: String) : CuentaError(message)
}