package org.example.butacas.repositorio

import org.example.butacas.mappers.toButaca
import org.example.butacas.models.Butaca
import org.example.cuenta.mappers.toLong
import org.example.database.manager.SqlDelightManager
import org.example.database.manager.logger
import org.koin.core.annotation.Singleton
import java.time.LocalDateTime

/**
 * Implementación del repositorio para el manejo de butacas en la base de datos.
 *
 * @property sqlDelightManager El administrador de SqlDelight para acceder a la base de datos.
 */
@Singleton
class ButacaRepositorioImpl (
    sqlDelightManager: SqlDelightManager
) : ButacaRepositorio{
    private val db = sqlDelightManager.databaseQueries

    /**
     * Recupera todas las butacas de la base de datos.
     *
     * @return Una lista de todas las butacas en la base de datos, o una lista vacía si no hay butacas.
     */
    override fun findAll(): List<Butaca> {
        logger.debug { "Buscando todas las butacas en la base de datos" }
        if (db.countButacas().executeAsOne() > 0){
            return db.getAllButacas().executeAsList().map {
                it.toButaca()
            }
        }
        return emptyList()
    }

    /**
     * Recupera todas las butacas de la base de datos antes de una fecha determinada.
     *
     * @param date La fecha a partir de la cual recuperar las butacas.
     * @return Una lista de todas las butacas en la base de datos, o una lista vacía si no hay butacas.
     */
    override fun findAllBasedOnDate(date: LocalDateTime): List<Butaca> {
        logger.debug { "Buscando todas las butacas en la base de datos antes de ${date.dayOfMonth}/${date.monthValue}/${date.year}" }
        if (db.countButacasBasedOnDate(date.toString()).executeAsOne() > 0){
            return db.getButacasBasedOnDate(date.toString()).executeAsList().map {
                it.toButaca()
            }
        }
        return emptyList()
    }

    /**
     * Busca y devuelve una butaca con el ID especificado..
     *
     * @param id El ID de la butaca a buscar..
     * @return Un objeto Butaca si se encuentra, o null si no se encuentra.
     */
    override fun findById(id: String): Butaca? {
        logger.debug { "Buscando una butaca con id: $id" }
        if (db.butacaExists(id).executeAsOne()){
            return db.getButacaById(id).executeAsOne().toButaca()
        }
        return null
    }

    /**
     * Guarda una nueva butaca en la base de datos.
     *
     * @param butaca La butaca a guardar.
     * @param ignoreKey Indica si se debe ignorar la clave primaria al guardar.
     * @return La butaca guardada si se guarda con éxito, o null si ya existe una butaca con el mismo ID y ignoreKey es false.
     */
    override fun save(butaca: Butaca, ignoreKey : Boolean): Butaca? {
        logger.debug { "Buscado la butaca con id: ${butaca.id}" }
        if (ignoreKey || findById(butaca.id) == null){
            db.insertButaca(
                id = butaca.id.uppercase(),
                tipo = butaca.tipo!!.name,
                estado = butaca.estado!!.name,
                ocupamiento = butaca.ocupamiento!!.name,
                createdAt = butaca.createdAt.toString(),
                updatedAt = LocalDateTime.now().toString(),
                isDeleted = butaca.isDeleted.toLong()
            )
            return butaca
        }
        return null
    }

    /**
     * Actualiza una butaca existente en la base de datos.
     *
     * @param id El ID de la butaca a actualizar.
     * @param butaca El objeto Butaca con los nuevos datos.
     * @return El objeto Butaca actualizado si se actualiza con éxito, o null si la butaca no existe.
     */
    override fun update(id: String, butaca: Butaca): Butaca? {
        logger.debug { "Actualizando la butaca con id: $id"}
        findById(id)?.let {
            val nuevaButaca = butaca.copy(
                id = id,
                estado = butaca.estado,
                ocupamiento = butaca.ocupamiento,
                updatedAt = LocalDateTime.now(),
                isDeleted = butaca.isDeleted
            )
            save(nuevaButaca, true)?.let { return nuevaButaca }
            return null
        }
        return null
    }

    /**
     * Elimina una butaca de la base de datos.
     *
     * @param id El ID de la butaca a eliminar.
     * @return El objeto Butaca eliminado si se elimina con éxito, o null si la butaca no existe.
     */
    override fun delete(id: String): Butaca? {
        logger.debug { "Borrando butaca con id: $id" }
        findById(id)?.let {
            val nuevaButaca = it.copy(
                id = id,
                estado = it.estado,
                ocupamiento = it.ocupamiento,
                updatedAt = LocalDateTime.now(),
                isDeleted = true
            )
            save(nuevaButaca, true)?.let { return nuevaButaca }
            return null
        }
        return null
    }

    /**
     * Busca y devuelve una butaca con el ID especificado y la fecha de actualización especificada.
     *
     * @param id El ID de la butaca a buscar.
     * @param date La fecha de actualización de la butaca.
     * @return Un objeto Butaca si se encuentra, o null si no se encuentra.
     */
    override fun findByIdAndDate(id: String, date: LocalDateTime): Butaca? {
        logger.debug { "Encontrando la butaca con id= $id en ${date.dayOfMonth}/${date.monthValue}/${date.year} ${date.hour}:${date.minute}:${date.second}" }
        if(db.butacaExistsOnACertainDate(id = id, updatedAt = date.toString()).executeAsOne()){
            return db.getButacaBasedOnIdAndDate(id = id, updatedAt = date.toString()).executeAsOne().toButaca()
        }
        return null
    }

}
