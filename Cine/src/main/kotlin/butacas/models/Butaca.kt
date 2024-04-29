package org.example.butacas.models

import java.time.LocalDateTime

/**
 * Clase que representa una butaca con sus atributos.
 *
 * @property id El identificador único de la butaca.
 * @property estado El estado actual de la butaca, puede ser ACTIVA, EN_MANTENIMIENTO o FUERA_SERVICIO.
 * @property ocupamiento El estado de ocupación de la butaca, puede ser LIBRE, RESERVADA u OCUPADA.
 * @property tipo El tipo de la butaca, puede ser VIP o NORMAL, con un precio asociado.
 * @property createdAt La fecha y hora de creación de la butaca, se establece en el momento de la creación por defecto.
 * @property updatedAt La fecha y hora de la última actualización de la butaca, se establece en el momento de la creación por defecto.
 * @property isDeleted Indica si la butaca ha sido marcada como eliminada, por defecto es false.
 */
data class Butaca (
    var id : String,
    var estado : Estado?,
    var ocupamiento: Ocupamiento?,
    var tipo : Tipo?,
    var createdAt : LocalDateTime = LocalDateTime.now(),
    var updatedAt : LocalDateTime = LocalDateTime.now(),
    var isDeleted : Boolean = false
){

}

enum class Estado{
    ACTIVA, EN_MANTENIMIENTO, FUERA_SERVICIO
}

enum class Ocupamiento{
    LIBRE, RESERVADA, OCUPADA
}

/**
 * @property precio El precio asociado al tipo de butaca.
 */
enum class Tipo(val precio : Int){
    VIP(8), NORMAL(5)
}