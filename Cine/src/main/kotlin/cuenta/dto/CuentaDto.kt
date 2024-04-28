package org.example.cuenta.dto

import java.time.LocalDateTime

/**
 * Clase de datos representando una cuenta bancaria DTO.
 *
 * @property id Identificador único de la cuenta.
 * @property createdAt El timestamp cuando se creó la cuenta.
 * @property updatedAt El timestamp cuando se actualizó por última vez la cuenta.
 * @property isDeleted Un flag que indica si la cuenta ha sido eliminada (0 para falso, 1 para verdadero).
 */
data class CuentaDTO(
    var id: String,
    var createdAt: String = LocalDateTime.now().toString(),
    var updatedAt: String = LocalDateTime.now().toString(),
    var isDeleted: Int = 0
)
