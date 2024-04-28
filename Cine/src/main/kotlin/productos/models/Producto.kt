package org.example.productos.models

import java.time.LocalDateTime
import java.util.UUID

/**
 * Representa un producto.
 * @property id El identificador único del producto.
 * @property nombre El nombre del producto.
 * @property precio El precio del producto.
 * @property stock El stock disponible del producto.
 * @property tipo El tipo de producto.
 * @property createdAt La fecha y hora de creación del producto.
 * @property updatedAt La fecha y hora de la última actualización del producto.
 * @property isDeleted Indica si el producto ha sido eliminado.
 */
data class Producto(
    val id: String = UUID.randomUUID().toString(),
    var nombre: String,
    var precio: Double,
    var stock: Int,
    var tipo: TipoProducto?,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var isDeleted: Boolean = false
)

/**
 * Enumera los posibles tipos de producto.
 */
enum class TipoProducto{
    BEBIDA, COMIDA, OTROS
}