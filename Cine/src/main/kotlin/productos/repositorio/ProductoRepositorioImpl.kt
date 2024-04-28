package org.example.productos.repositorio

import org.example.cuenta.mappers.toLong
import org.example.database.manager.SqlDelightManager
import org.example.database.manager.logger
import org.example.productos.mappers.toProducto
import org.example.productos.models.Producto
import org.koin.core.annotation.Singleton
import java.time.LocalDateTime

/**
 * Implementación del repositorio de productos que interactúa con la base de datos.
 * @param sqlDelightManager El gestor de SQLDelight para acceder a la base de datos.
 */
@Singleton
class ProductoRepositorioImpl(
    sqlDelightManager: SqlDelightManager
) : ProductosRepositorio {
    private val db = sqlDelightManager.databaseQueries

    /**
     * Obtiene todos los productos de la base de datos.
     * @return Una lista de productos.
     */
    override fun findAll(): List<Producto> {
        logger.debug { "Buscando todos los productos en la base de datos" }
        if (db.countProductos().executeAsOne() > 0){
            return db.getAllProductos().executeAsList().map {
                it.toProducto()
            }
        }
        return emptyList()
    }

    /**
     * Encuentra un producto por su ID.
     * @param id El ID del producto a buscar.
     * @return El producto encontrado o null si no se encontró ningún producto con el ID dado.
     */
    override fun findById(id: String): Producto? {
        logger.debug { "Buscando un producto con id: $id" }
        if (db.productoExists(id).executeAsOne()){
            return db.getProductoById(id).executeAsOne().toProducto()
        }
        return null
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     * @param producto El producto a guardar.
     * @param ignoreKey Indica si se debe ignorar el ID del producto.
     * @return El producto guardado o null si no se pudo guardar.
     */
    override fun save(producto: Producto, ignoreKey : Boolean) : Producto? {
        logger.debug { "Añadiendo el producto: '${producto.nombre}' al inventario" }
        if (ignoreKey || findById(producto.id) == null){
            db.insertComplemento(
                id = producto.id,
                nombre = producto.nombre,
                precio = producto.precio,
                stock = producto.stock.toLong(),
                tipo = producto.tipo.toString(),
                createdAt = producto.createdAt.toString(),
                updatedAt = producto.updatedAt.toString(),
                isDeleted = producto.isDeleted.toLong()
            )
            return producto
        }
        return null
    }

    /**
     * Actualiza un producto existente en la base de datos.
     * @param id El ID del producto a actualizar.
     * @param producto El nuevo estado del producto.
     * @return El producto actualizado o null si no se pudo actualizar.
     */
    override fun update(id: String, producto: Producto): Producto? {
        logger.debug { "Actualizando el producto con id: $id"}
        val nuevoProducto = producto.copy(
            nombre = producto.nombre,
            precio = producto.precio,
            stock = producto.stock,
            tipo = producto.tipo!!,
            updatedAt = LocalDateTime.now(),
            isDeleted = producto.isDeleted,
        )
        save(nuevoProducto, true)?.let { return nuevoProducto }
        return null
    }

    /**
     * Elimina un producto de la base de datos.
     * @param id El ID del producto a eliminar.
     * @return El producto eliminado o null si no se pudo eliminar.
     */
    override fun delete(id: String): Producto? {
        logger.debug { "Borrando Producto con id: $id" }
        findById(id)?.let {
            val nuevoProducto = it.copy(
                nombre = it.nombre,
                precio = it.precio,
                stock = it.stock,
                tipo = it.tipo!!,
                updatedAt = LocalDateTime.now(),
                isDeleted = true,
            )
            save(nuevoProducto,true)?.let { return it }
            return null
        }
        return null
    }

    /**
     * Encuentra un producto por su ID y fecha.
     * @param id El ID del producto a buscar.
     * @param date La fecha que queremos buscar
     * @return El producto encontrado o null si no se encontró ningún producto con el ID dado.
     */
    override fun findByIdAndDate(date: LocalDateTime, id: String): Producto? {
        logger.debug { "Borrando Producto con id: $id en ${date.dayOfMonth}:${date.monthValue}:${date.year} " }
        if (db.productoExists(id).executeAsOne()){
            return db.findProductByIdAndDate(id = id, updatedAt = date.toString()).executeAsOne().toProducto()
        }
        return null
    }

}