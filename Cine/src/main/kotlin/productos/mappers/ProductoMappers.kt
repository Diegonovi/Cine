package org.example.productos.mappers

import database.ComplementoEntity
import org.example.productos.models.Producto
import org.example.productos.models.TipoProducto
import org.example.ventas.mappers.toVenta
import java.time.LocalDateTime

/**
 * Convierte un [ComplementoEntity] en un [Producto].
 * @return El producto convertido.
 */
fun ComplementoEntity.toProducto(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        tipo = elegirTipoProducto(this.tipo),
        stock = this.stock.toInt(),
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.createdAt),
        isDeleted = this.isDeleted.toInt() == 1
    )
}

/**
 * Determina el [TipoProducto] basado en una cadena.
 * @param s La cadena que representa el tipo de producto.
 * @return El tipo de producto correspondiente, o null si no se reconoce la cadena.
 */
fun elegirTipoProducto(s: String): TipoProducto? {
    return when (s) {
        "BEBIDA" -> TipoProducto.BEBIDA
        "COMIDA" -> TipoProducto.COMIDA
        "OTROS" -> TipoProducto.OTROS
        else -> null
    }
}