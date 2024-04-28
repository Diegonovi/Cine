package org.example.ventas.respositorio

import org.example.butacas.repositorio.ButacaRepositorio
import org.example.cuenta.mappers.toLong
import org.example.cuenta.repositorio.CuentaRepositorio
import org.example.database.manager.SqlDelightManager
import org.example.database.manager.logger
import org.example.productos.repositorio.ProductosRepositorio
import org.example.ventas.mappers.toLineaVenta
import org.example.ventas.mappers.toVenta
import org.example.ventas.models.LineaVenta
import org.example.ventas.models.Venta
import org.koin.core.annotation.Singleton
import java.time.LocalDateTime

/**
 * Implementación del repositorio de ventas que interactúa con la base de datos.
 * @property sqlDelightManager Gestor de SqlDelight para acceder a la base de datos.
 * @property productosRepositorio Repositorio de productos para acceder a la información de los productos.
 * @property clienteRepositorio Repositorio de cuentas de cliente para acceder a la información del cliente.
 * @property butacaRepositorio Repositorio de butacas para acceder a la información de las butacas.
 */
@Singleton
class VentaRepositorioImpl(
    private val sqlDelightManager: SqlDelightManager,
    private val productosRepositorio: ProductosRepositorio,
    private val clienteRepositorio : CuentaRepositorio,
    private val butacaRepositorio: ButacaRepositorio
) : VentaRepositorio{

    private var db = sqlDelightManager.databaseQueries

    /**
     * Obtiene todas las ventas almacenadas en la base de datos.
     * @return Lista de ventas encontradas.
     */
    override fun findAll(): List<Venta> {
        logger.debug { "Buscando todas las ventas en la base de datos" }
        if (db.countVentas().executeAsOne() > 0){ //Para evitar que executeAsList te de una excepcion
            return db
                .getAllVentas()
                .executeAsList()
                .map{
                    val lineas =  getAllLineasByVentaId(it.id)
                    val butaca = butacaRepositorio.findById(it.id_butaca)
                    val cliente = clienteRepositorio.findById(it.id_socio)
                    it.toVenta(lineas = lineas, butaca = butaca!!, cliente = cliente!!)
                }
        }
        return emptyList()
    }

    /**
     * @param id el id de la linea de venta que está utilizando para buscar las lineas de venta
     */
    private fun getAllLineasByVentaId(id : String) : List<LineaVenta>{
        if (db.countLineasVentaByVentaId(id).executeAsOne() > 0){
            return db.getLineaVentaByVentaId(id)
                .executeAsList()
                .map {
                    val producto = productosRepositorio.findById(it.id_complemento)
                    it.toLineaVenta(producto!!)
                }
        }
        return emptyList()
    }

    /**
     * Obtiene una venta específica según su identificador.
     * @param id Identificador único de la venta.
     * @return La venta encontrada, o null si no se encuentra.
     */
    override fun findById(id: String): Venta? {
        logger.debug { "Buscando la venta con id : $id" }
        if (db.existsVenta(id).executeAsOne()){
            val ventaEntity = db.getVentaById(id).executeAsOne()
            val lineas = getAllLineasByVentaId(ventaEntity.id).filter { !it.isDeleted }
            val butaca = butacaRepositorio.findById(ventaEntity.id_butaca)
            val cliente = clienteRepositorio.findById(ventaEntity.id_socio)
            return ventaEntity.toVenta(lineas,butaca!!, cliente!!)
        }
        return null
    }

    /**
     * Guarda una venta en la base de datos.
     * @param venta La venta a guardar.
     * @param ignoreKey Indica si se debe ignorar la clave única al guardar.
     * @return La venta guardada, o null si ya existe una venta con la misma clave única y ignoreKey es false.
     */
    override fun save(venta: Venta, ignoreKey : Boolean): Venta? {
        logger.debug { "Guardando venta con id: ${venta.id}" }
        if (ignoreKey || findById(venta.id) == null){
            db.insertVenta(
                id = venta.id,
                id_socio = venta.cliente.id,
                id_butaca = venta.butaca.id,
                updatedAt = LocalDateTime.now().toString(),
                createdAt = venta.createdAt.toString(),
                isDeleted = venta.isDeleted.toLong()
            )
            venta.lineasVenta.forEach {
                db.insertLineaVenta(
                    id = it.id,
                    id_venta = venta.id,
                    id_complemento = it.producto.id,
                    precio = it.precio,
                    cantidad = it.cantidad.toLong(),
                    createdAt = it.createdAt.toString(),
                    updatedAt = LocalDateTime.now().toString(),
                    isDeleted = it.isDeleted.toLong()
                )
            }
            return venta
        }
        return null
    }

    /**
     * Actualiza una venta existente en la base de datos.
     * @param id Identificador único de la venta a actualizar.
     * @param venta Nueva información de la venta.
     * @return La venta actualizada, o null si la venta no existe.
     */
    override fun update(id: String, venta: Venta): Venta? {
        logger.debug { "Actualizando venta con id: $id" }
        findById(id)?.let { //Si existe
            val nuevaVenta = Venta(
                butaca = venta.butaca,
                cliente = venta.cliente,
                isDeleted = venta.isDeleted,
                createdAt = venta.createdAt,
                updatedAt = LocalDateTime.now(),
                id = id,
                lineasVenta = venta.lineasVenta
            )
            it.lineasVenta.forEach {
                deleteLineaVenta(it)
            }
            save(nuevaVenta, true)?.let { return nuevaVenta }
            return null
        }
        return null
    }

    /**
     * Elimina una venta de la base de datos.
     * @param id Identificador único de la venta a eliminar.
     * @return La venta eliminada, o null si la venta no existe.
     */
    override fun delete(id: String): Venta? {
        logger.debug { "Borrando venta con id: $id" }
        findById(id)?.let { //Si existe
            val nuevaVenta = Venta(
                butaca = it.butaca,
                cliente = it.cliente,
                isDeleted = true,
                createdAt = it.createdAt,
                updatedAt = LocalDateTime.now(),
                id = id,
                lineasVenta = it.lineasVenta
            )
            save(nuevaVenta, true)
                ?.let {
                    it.lineasVenta.forEach {
                        deleteLineaVenta(it)
                    }
                    return nuevaVenta
                }
            return null
        }
        return null
    }

    /**
     * Obtiene todas las ventas realizadas en una fecha específica.
     * @param date Fecha para la cual buscar las ventas.
     * @return Lista de ventas realizadas en la fecha especificada.
     */
    override fun findAllByDate(date: LocalDateTime): List<Venta> {
        logger.debug { "Buscando las ventas en: ${date.dayOfMonth}/${date.monthValue}/${date.year} ${date.hour}:${date.minute}:${date.second}" }
        if (db.countVentasByDate(date.toString()).executeAsOne() > 0){
            return db
                .getVentasByDate(date.toString())
                .executeAsList()
                .map {
                    val lineas = getAllLineasByVentaIdAndDate(it.id,date)
                    val butaca = butacaRepositorio.findByIdAndDate(it.id_butaca,date)
                    val cliente = clienteRepositorio.findById(it.id_socio)
                    it.toVenta(lineas = lineas, butaca = butaca!!, cliente = cliente!!)
                }
        }
        return emptyList()
    }

    /**
     * Elimina una línea de venta de la base de datos.
     * @param lineaVenta La línea de venta a eliminar.
     * @return La línea de venta eliminada.
     */
    override fun deleteLineaVenta(lineaVenta: LineaVenta): LineaVenta {
        db.deleteLineaVenta(lineaVenta.id,lineaVenta.id)
        return lineaVenta
    }

    /**
     * Obtiene todas las líneas de venta asociadas a una venta y a una fecha específica.
     * @param id Identificador único de la venta.
     * @param date Fecha para la cual buscar las líneas de venta.
     * @return Lista de líneas de venta asociadas a la venta y fecha especificadas.
     */
    private fun getAllLineasByVentaIdAndDate(id : String, date: LocalDateTime) : List<LineaVenta>{
        if (db.countLineaVentaByVentaIdAndDate(id_venta = id, updatedAt = date.toString()).executeAsOne() > 0){
            return db.getLineaVentaByVentaIdAndDate(id_venta = id, updatedAt = date.toString())
                .executeAsList()
                .map {
                    val producto = productosRepositorio.findByIdAndDate(id = it.id_complemento, date = date)
                    it.toLineaVenta(producto!!)
                }
        }
        return emptyList()
    }


}