package org.example.ventas.mappers

import database.LineaVentaEntity
import database.VentaEntity
import org.example.butacas.models.Butaca
import org.example.cuenta.models.Cuenta
import org.example.productos.models.Producto
import org.example.ventas.models.LineaVenta
import org.example.ventas.models.Venta
import java.time.LocalDateTime

/**
 * Convierte una entidad de línea de venta a un objeto LineaVenta.
 * @param producto El producto asociado con la línea de venta.
 * @return Un objeto LineaVenta.
 */
fun LineaVentaEntity.toLineaVenta(producto: Producto) : LineaVenta{
    return LineaVenta(
        id = this.id,
        cantidad = cantidad.toInt(),
        precio = precio,
        producto = producto,
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.createdAt),
        isDeleted = this.isDeleted.toInt() == 1
    )
}

/**
 * Convierte una entidad de venta a un objeto Venta.
 * @param lineas La lista de líneas de venta asociadas con la venta.
 * @param butaca La butaca asociada con la venta.
 * @param cliente El cliente asociado con la venta.
 * @return Un objeto Venta.
 */
fun VentaEntity.toVenta(
    lineas : List<LineaVenta>,
    butaca: Butaca,
    cliente : Cuenta
) : Venta{
    return Venta(
        id = this.id,
        lineasVenta = lineas,
        cliente = cliente,
        butaca = butaca,
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.createdAt),
        isDeleted = this.isDeleted.toInt() == 1
    )
}