package org.example.butacas.storage

import com.github.michaelbull.result.Result
import org.example.productos.errors.ProductoError
import org.example.productos.models.Producto
import java.io.File

interface ProductoStorage {
    fun cargar(file : File) : Result<List<Producto>, ProductoError>
    fun exportar(list: List<Producto>) : Result<Unit, ProductoError>
}