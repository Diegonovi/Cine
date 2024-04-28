package org.example.productos.repositorio

import org.example.productos.models.Producto
import java.time.LocalDateTime

interface ProductosRepositorio {
    fun findAll(): List<Producto>
    fun findById(id: String): Producto?
    fun save(producto: Producto, ignoreKey : Boolean = false): Producto?
    fun update(id: String, producto: Producto): Producto?
    fun delete(id: String): Producto?
    fun findByIdAndDate(date: LocalDateTime, id: String) : Producto?
}