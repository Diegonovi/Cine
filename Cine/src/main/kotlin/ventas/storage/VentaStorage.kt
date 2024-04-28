package org.example.ventas.storage

import com.github.michaelbull.result.Result
import org.example.butacas.errors.ButacaError
import org.example.ventas.errors.VentaError
import org.example.ventas.models.Venta
import java.io.File

interface VentaStorage {
    fun cargar(file : File) : Result<List<Venta>, ButacaError>
    fun exportar(venta: Venta) : Result<Unit,VentaError>
}