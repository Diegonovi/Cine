package org.example.productos.servicio

import com.github.michaelbull.result.*
import org.example.productos.errors.ProductoError
import org.example.butacas.storage.ProductoStorage
import org.example.config.Config
import org.example.database.manager.logger
import org.example.productos.models.Producto
import org.example.productos.repositorio.ProductosRepositorio
import org.example.productos.validador.ProductoValidador
import org.koin.core.annotation.Singleton
import java.io.File

/**
 * Implementación del servicio de productos.
 * @param productosRepositorio El repositorio de productos.
 * @param productoValidador El validador de productos.
 * @param productoStorage El almacenamiento de productos.
 * @param config La configuración de la aplicación.
 */
@Singleton
class ProductoServicioImpl(
    private var productosRepositorio: ProductosRepositorio,
    private var productoValidador: ProductoValidador,
    private var productoStorage: ProductoStorage,
    private var config: Config
) : ProductoServicio {

    /**
     * Guarda un nuevo producto.
     * @param producto El producto a guardar.
     * @return Un resultado que contiene el producto guardado o un error.
     */
    override fun save(producto: Producto) : Result<Producto, ProductoError> {
        productoValidador.validate(producto) //Para esegurarse que es un producto válido
            .onSuccess {
                productosRepositorio.save(producto)?.let {
                    return Ok(producto)
                }
                return Err(ProductoError.ProductoStorageError("El producto no se pudo guardar en la base de datos"))
            }
        return Err(ProductoError.ProductoStorageError("No se pudo guardar el producto con id: ${producto.id}"))
    }

    /**
     * Obtiene todos los productos.
     * @return Un resultado que contiene una lista de productos o un error.
     */
    override fun findAll(): Result<List<Producto>, ProductoError> {
        val result = productosRepositorio.findAll()
        if (result.isNotEmpty()) return Ok(result)
        else return Err(ProductoError.ProductoStorageError("No hay ningún producto en la base de datos"))
    }

    /**
     * Encuentra un producto por su ID.
     * @param id El ID del producto a buscar.
     * @return Un resultado que contiene el producto encontrado o un error si no se encontró ningún producto con el ID dado.
     */
    override fun findById(id: String): Result<Producto, ProductoError> {
        val producto = productosRepositorio.findById(id)
        return if (producto != null) {
            Ok(producto)
        } else {
            Err(ProductoError.ProductoNotFoundError("El producto con ID $id no existe"))
        }
    }

    /**
     * Actualiza un producto existente.
     * @param id El ID del producto a actualizar.
     * @param producto El nuevo estado del producto.
     * @return Un resultado que contiene el producto actualizado o un error si no se pudo actualizar.
     */
    override fun update(id: String, producto: Producto): Result<Producto, ProductoError> {
        val existingProducto = productosRepositorio.findById(id)
        return if (existingProducto != null) {
            val updatedProducto = producto.copy(id = id)
            val result = productosRepositorio.update(id, updatedProducto)
            if (result != null) {
                Ok(updatedProducto)
            } else {
                Err(ProductoError.ProductoStorageError("No se pudo actualizar el Producto"))
            }
        } else {
            Err(ProductoError.ProductoNotFoundError("El producto con ID $id no existe"))
        }
    }

    /**
     * Carga todos los productos desde un archivo.
     * @return Un resultado que contiene una lista de productos cargados o un error si hubo algún problema durante la carga.
     */
    override fun cargarTodosProductos() : Result<List<Producto>, ProductoError>{
        logger.debug { "Importando datos de productos" }
        val url = ClassLoader.getSystemResource(config.productoImportFile)
        if (url != null){
            return productoStorage.cargar(File(url.toURI()))
        }else return Err(ProductoError.ProductoStorageError("Hubo un problema cargando la lista de productos"))
    }
}