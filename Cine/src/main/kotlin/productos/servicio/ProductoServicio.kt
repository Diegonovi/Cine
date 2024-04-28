package org.example.productos.servicio

import com.github.michaelbull.result.Result
import org.example.productos.errors.ProductoError
import org.example.productos.models.Producto

interface ProductoServicio {
    fun save(producto: Producto) : Result<Producto, ProductoError>
    fun findAll() : Result<List<Producto>, ProductoError>
    fun findById(id : String) : Result<Producto, ProductoError>
    fun update(id: String, producto: Producto): Result<Producto, ProductoError>
    fun cargarTodosProductos() : Result<List<Producto>, ProductoError>
}