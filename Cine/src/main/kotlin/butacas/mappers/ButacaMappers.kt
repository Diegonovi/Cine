package org.example.butacas.mappers

import database.ButacaEntity
import org.example.butacas.dto.ButacaDto
import org.example.butacas.models.Butaca
import org.example.butacas.models.Estado
import org.example.butacas.models.Ocupamiento
import org.example.butacas.models.Tipo
import java.time.LocalDateTime

/**
 * Convierte una entidad de butaca a un objeto butaca.
 * @return Un objeto de modelo de butaca.
 */
fun ButacaEntity.toButaca() : Butaca{
    return Butaca(
        id = this.id,
        estado = elegirEstado(this.estado),
        ocupamiento = elegirOcupamiento(this.ocupamiento),
        tipo = elegirTipo(this.tipo),
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.createdAt),
        isDeleted = this.isDeleted.toInt() == 1
    )
}

/**
 * Convierte una cadena que representa el tipo de butaca.
 *
 * @param s La cadena que representa el tipo de butaca.
 * @return El tipo de butaca correspondiente, o null.
 */
fun elegirTipo(s: String): Tipo? {
    return when(s){
        "NORMAL" -> Tipo.NORMAL
        "VIP" -> Tipo.VIP
        else -> null
    }
}

/**
 * Convierte una cadena que representa el estado de ocupación.
 *
 * @param s La cadena que representa el estado de ocupación.
 * @return El estado de ocupación correspondiente, o null.
 */
fun elegirOcupamiento(s: String): Ocupamiento? {
    return when(s){
        "OCUPADA" -> Ocupamiento.OCUPADA
        "RESERVADA" -> Ocupamiento.RESERVADA
        "LIBRE" -> Ocupamiento.LIBRE
        else -> null
    }
}

/**
 * Convierte una cadena que representa el estado de una butaca.
 *
 * @param s La cadena que representa el estado de la butaca.
 * @return El estado de la butaca correspondiente, o null.
 */
fun elegirEstado(s: String): Estado? {
    return when(s){
        "ACTIVA" -> Estado.ACTIVA
        "FUERA_SERVICIO" -> Estado.FUERA_SERVICIO
        "EN_MANTENIMIENTO" -> Estado.EN_MANTENIMIENTO
        else -> null
    }
}

/**
 * Convierte un objeto de modelo de butaca a un objeto DTO de butaca.
 *
 * @return Un objeto DTO de butaca.
 */
fun Butaca.toButacaDTO() : ButacaDto{
    return ButacaDto(
        id = this.id,
        tipo = this.tipo!!.name,
        estado = this.estado!!.name,
        ocupamiento = this.ocupamiento!!.name,
        createdAt = "${this.createdAt.dayOfMonth}/${this.createdAt.monthValue}/${this.createdAt.year} ${this.createdAt.hour}:${this.createdAt.minute}:${this.createdAt.second}",
        updatedAt = "${this.updatedAt.dayOfMonth}/${this.updatedAt.monthValue}/${this.updatedAt.year} ${this.updatedAt.hour}:${this.updatedAt.minute}:${this.updatedAt.second}"
    )
}