package org.example.butacas.dto

import kotlinx.serialization.Serializable

/**
 * Clase que representa una butaca con sus atributos.
 *
 * @property id El identificador único de la butaca.
 * @property estado actual de la butaca.
 * @property ocupamiento libre o ocupada.
 * @property tipo de la butaca.
 * @property createdAt La fecha y hora de creación de la butaca.
 * @property updatedAt La fecha y hora de la última actualización de la butaca.
 * @property isDeleted Indica si la butaca ha sido marcada como eliminada.
 */
@Serializable
class ButacaDto (
    var id : String,
    var estado : String,
    var ocupamiento: String,
    var tipo : String,
    var createdAt : String,
    var updatedAt : String,
    var isDeleted : Boolean = false
){
}