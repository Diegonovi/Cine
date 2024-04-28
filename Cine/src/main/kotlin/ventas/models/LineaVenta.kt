package org.example.ventas.models

import org.example.productos.models.Producto
import java.time.LocalDateTime
import java.util.UUID

/**
 * Clase que representa una línea de venta.
 * @property id El ID único de la línea de venta.
 * @property producto El producto asociado con la línea de venta.
 * @property cantidad La cantidad del producto vendido.
 * @property precio El precio unitario del producto.
 * @property createdAt La fecha y hora de creación de la línea de venta.
 * @property updatedAt La fecha y hora de la última actualización de la línea de venta.
 * @property isDeleted Indica si la línea de venta ha sido eliminada.
 */
data class LineaVenta(
    var id : String = UUID.randomUUID().toString(),
    var producto: Producto,
    var cantidad : Int,
    var precio : Double,
    var createdAt : LocalDateTime = LocalDateTime.now(),
    var updatedAt : LocalDateTime = LocalDateTime.now(),
    var isDeleted : Boolean = false
) {
}