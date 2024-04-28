package org.example.cuenta.models

import java.time.LocalDateTime

/**
 * La clase Cuenta representa una cuenta de usuario.
 * @property id El identificador único de la cuenta.
 * @property createdAt La fecha y hora de creación de la cuenta, por defecto es el momento actual.
 * @property updatedAt La fecha y hora de la última actualización de la cuenta, por defecto es el momento actual.
 * @property isDeleted Indica si la cuenta ha sido eliminada (true) o no (false), por defecto es falso.
 */
data class Cuenta (
    var id : String,
    var createdAt : LocalDateTime = LocalDateTime.now(),
    var updatedAt : LocalDateTime = LocalDateTime.now(),
    var isDeleted : Boolean = false
)