package org.example.ventas.servicio

import com.github.michaelbull.result.*
import org.example.ventas.storage.VentaStorage
import org.example.ventas.errors.VentaError
import org.example.ventas.models.Venta
import org.example.ventas.respositorio.VentaRepositorio
import org.koin.core.annotation.Singleton
import java.time.LocalDateTime

/**
 * Implementación del servicio de gestión de ventas.
 * @property ventaRepositorio Repositorio de ventas para interactuar con la base de datos.
 * @property ventaStorage Almacenamiento de ventas para exportar las ventas.
 */
@Singleton
class VentaServicioImpl (
    var ventaRepositorio: VentaRepositorio,
    var ventaStorage: VentaStorage
) : VentaServicio {


    /**
     * Guarda una venta en el repositorio de ventas.
     * @param venta La venta a guardar.
     * @return [Result] que contiene la venta guardada en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun save(venta: Venta): Result<Venta, VentaError> {
        ventaRepositorio.save(venta)?.let {
            return Ok(it)
        }
        return Err(VentaError.VentaStorageError("No se ha podido guardar la venta con id: ${venta.id}"))
    }

    /**
     * Obtiene todas las ventas almacenadas en el repositorio.
     * @return [Result] que contiene la lista de ventas encontradas en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun findAll(): Result<List<Venta>, VentaError> {
        return Ok(ventaRepositorio.findAll().filter { !it.isDeleted })
    }

    /**
     * Busca una venta por su identificador único.
     * @param id Identificador único de la venta.
     * @return [Result] que contiene la venta encontrada en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun findById(id: String): Result<Venta, VentaError> {
        ventaRepositorio.findById(id)?.let {
            return Ok(it)
        }
        return Err(VentaError.VentaStorageError("No existe ninguna venta con el id: $id en la base de datos"))
    }

    /**
     * Actualiza una venta existente en el repositorio.
     * @param id Identificador único de la venta a actualizar.
     * @param venta Nueva información de la venta.
     * @return [Result] que contiene la venta actualizada en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun update(id: String, venta: Venta): Result<Venta, VentaError> {
        ventaRepositorio.update(id,venta)?.let {
            return Ok(it)
        }
        return Err(VentaError.VentaStorageError("No existe ninguna venta con el id: $id en la base de datos"))
    }

    /**
     * Obtiene todas las ventas realizadas en una fecha específica.
     * @param date Fecha para la cual buscar las ventas.
     * @return [Result] que contiene la lista de ventas encontradas en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun findAllByDate(date: LocalDateTime): Result<List<Venta>, VentaError> {
        return Ok(ventaRepositorio.findAllByDate(date))
    }

    /**
     * Exporta una venta al almacenamiento de ventas.
     * @param venta La venta a exportar.
     * @return [Result] que contiene Unit en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun exportVenta(venta: Venta): Result<Unit, VentaError>{
        ventaStorage.exportar(venta).onFailure {
            return Err(it)
        }
        return Ok(Unit)
    }

    /**
     * Obtiene todas las ventas realizadas en una fecha específica.
     * @param date Fecha para la cual buscar las ventas.
     * @return [Result] que contiene la lista de ventas encontradas en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun getAllVentasByDate(date : LocalDateTime): Result<List<Venta>, VentaError> {
        return Ok(ventaRepositorio.findAllByDate(date))
    }

    /**
     * Elimina una venta del repositorio.
     * @param id Identificador único de la venta a eliminar.
     * @return [Result] que contiene la venta eliminada en caso de éxito, o un [VentaError] en caso de error.
     */
    override fun delete(id: String): Result<Venta, VentaError> {
        ventaRepositorio.delete(id)?.let {
            return Ok(it)
        }
        return Err(VentaError.VentaStorageError("La venta que está intentando borrar no existe"))
    }

    /**
     * Elimina una venta del repositorio.
     * @param id Identificador del cliente
     * @return [Result] que contiene las ventas de un cliente o un [VentaError] en caso de error.
     */
    override fun findVentasByClienteId(id: String): Result<List<Venta>, VentaError> {
        return Ok(ventaRepositorio.findAll().filter { it.cliente.id == id && !it.isDeleted})
    }


}