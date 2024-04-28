package org.example.cuenta.mappers

import org.example.cuenta.dto.CuentaDTO
import org.example.cuenta.models.Cuenta
import java.time.LocalDateTime

/**
 * Convierte un objeto [CuentaDTO] en un objeto [Cuenta].
 * @return el objeto [Cuenta] resultante.
 */
fun CuentaDTO.toCuenta() : Cuenta {
    return Cuenta(
        id = this.id,
        isDeleted = isDeleted == 1,
        updatedAt = LocalDateTime.parse(this.updatedAt),
        createdAt = LocalDateTime.parse(this.createdAt)
    )
}

/**
 * Convierte un objeto [Cuenta] en un objeto [CuentaDTO].
 * @return el objeto [CuentaDTO] resultante.
 */
fun Cuenta.toCuentaDto() : CuentaDTO{
    return CuentaDTO(
        id = this.id,
        createdAt = this.toString(),
        updatedAt = this.toString(),
        isDeleted = this.isDeleted.toLong().toInt()
    )
}

/**
 * Convierte un valor booleano en un valor Long.
 * @return 1 si el valor booleano es verdadero, 0 si es falso.
 */
fun Boolean.toLong(): Long {
    if (this) return 1
    else return 0
}

