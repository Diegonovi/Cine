package org.example.productos.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.productos.errors.ProductoError
import org.example.butacas.storage.ProductoStorage
import org.example.database.manager.logger
import org.example.productos.mappers.elegirTipoProducto
import org.example.productos.models.Producto
import org.koin.core.annotation.Singleton
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

/**
 * Implementación del almacenamiento de productos desde y hacia archivos CSV.
 */
@Singleton
class ProductoStorageCSVImpl : ProductoStorage {
    /**
     * Carga productos desde un archivo CSV.
     * @param file El archivo CSV que contiene los productos.
     * @return Un resultado que contiene una lista de productos cargados o un error si hubo algún problema durante la carga.
     */
    override fun cargar(file: File): Result<List<Producto>, ProductoError> {
        try {
            return Ok(
                file.readLines()
                    .drop(1)
                    .map {
                        val producto = it.split(',')
                        Producto(
                            id = UUID.randomUUID().toString(),
                            nombre = producto[0],
                            stock = producto[1].toInt(),
                            tipo = elegirTipoProducto(producto[2]),
                            precio = producto[3].toDouble(),
                            isDeleted = false,
                            updatedAt = LocalDateTime.now(),
                            createdAt = LocalDateTime.now()
                        )
                    }
            )
        }catch (e : Exception){
            logger.debug { "Hubo un error al cargar las butacas del archivo ${file.name}" }
            return Err(ProductoError.ProductoStorageError("Hubo un error al cargar los productos del archivo ${file.name}"))
        }
    }

    override fun exportar(list: List<Producto>): Result<Unit, ProductoError> {
        println("Esta funcion no está implementada")
        return Ok(Unit)
    }
}