package org.example.ventas.servicio

import com.github.michaelbull.result.Result
import org.example.ventas.errors.VentaError
import org.example.ventas.models.Venta
import java.time.LocalDateTime

interface VentaServicio {
    fun save(venta: Venta) : Result<Venta,VentaError>
    fun findAll() : Result<List<Venta>, VentaError>
    fun findById(id : String) : Result<Venta, VentaError>
    fun update(id: String, venta : Venta): Result<Venta, VentaError>
    fun findAllByDate(date : LocalDateTime) : Result<List<Venta>, VentaError>
    fun exportVenta(venta: Venta) : Result<Unit,VentaError>
    fun getAllVentasByDate(date : LocalDateTime) : Result<List<Venta>,VentaError>
    fun delete(id: String) : Result<Venta,VentaError>
    fun findVentasByClienteId(id : String) : Result<List<Venta>,VentaError>
}