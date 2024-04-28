package org.example.butacas.servicios

import com.github.michaelbull.result.*
import org.example.butacas.errors.ButacaError
import org.example.butacas.models.Butaca
import org.example.butacas.repositorio.ButacaRepositorio
import org.example.butacas.storage.ButacaStorage
import org.example.butacas.validator.ButacaValidator
import org.example.config.Config
import org.example.database.manager.logger
import java.io.File
import java.time.LocalDateTime

/**
 * Implementación de los servicios relacionados con las butacas.
 *
 * @property butacaRepositorio El repositorio de butacas para acceder a la base de datos.
 * @property butacaStorageOut El almacenamiento de butacas para exportar datos.
 * @property butacaStorageIn El almacenamiento de butacas para importar datos.
 * @property butacaValidator El validador de butacas para validar los datos importados.
 * @property config La configuración de la aplicación.
 */
class ButacaServiceImpl(
    var butacaRepositorio: ButacaRepositorio,
    var butacaStorageOut: ButacaStorage,
    var butacaStorageIn: ButacaStorage,
    var butacaValidator: ButacaValidator,
    val config : Config
) : ButacaService {

    /**
     * Busca y devuelve todas las butacas en la base de datos.
     *
     * @return Un resultado que contiene una lista de objetos Butaca si la operación tiene éxito,
     * o un error de ButacaError si no se encuentran butacas.
     */
    override fun findAll(): Result<List<Butaca>, ButacaError> {
        val result = butacaRepositorio.findAll()
        if (result.isNotEmpty()) return Ok(result)
        else return Err(ButacaError.ButacaStorageError("No hay ninguna butaca en la base de datos"))
    }


    /**
     * Busca y devuelve una butaca en la base de datos.
     *
     * @param id El ID de la butaca a buscar.
     * @return Un resultado que contiene una butaca si la operación tiene éxito,
     */
    override fun findById(id: String): Result<Butaca, ButacaError> {
        val butaca = butacaRepositorio.findById(id)
        return if (butaca != null) {
            Ok(butaca)
        } else {
            Err(ButacaError.ButacaNotFoundError("La butaca con ID $id no existe"))
        }
    }


    /**
     * Crea una butaca en la base de datos.
     *
     * @param butaca La butaca a crear.
     */
    override fun update(id: String, butaca: Butaca): Result<Butaca, ButacaError> {
        val existingButaca = butacaRepositorio.findById(id)
        return if (existingButaca != null) {
            val updatedButaca = butaca.copy(id = id)
            val result = butacaRepositorio.update(id, updatedButaca)
            if (result != null) {
                Ok(updatedButaca)
            } else {
                Err(ButacaError.ButacaStorageError("No se pudo actualizar la butaca"))
            }
        } else {
            Err(ButacaError.ButacaNotFoundError("La butaca con ID $id no existe"))
        }
    }

    /**
     * Busca y devuelve todas las butacas en la base de datos basandose en la fecha.
     *
     * @return Un resultado que contiene una lista de objetos Butaca si la operación tiene éxito,
     * o un error de ButacaError si no se encuentran butacas.
     */
    override fun findAllByDate(date: LocalDateTime): Result<List<Butaca>, ButacaError> {
        val result = butacaRepositorio.findAllBasedOnDate(date)
        if (result.isNotEmpty()) return Ok(result)
        else return Err(ButacaError.ButacaStorageError("No hay ninguna butaca en la base de datos"))
    }

    /**
     * Exporta todos los datos de las butacas
     *
     * @return unit o da error
     */
    override fun exportAllToFile(date: LocalDateTime): Result<Unit, ButacaError> {
        val list = butacaRepositorio.findAllBasedOnDate(date)
        if (list.isEmpty()) return Err(
            ButacaError.ButacaStorageError("No hay butacas creadas antes de ${date.dayOfMonth}/${date.monthValue}/${date.year}")
        )else return butacaStorageOut.exportar(list)
    }

    /**
     * Importa datos de butacas.
     *
     * @return Un resultado que contiene `Unit` si la importación es exitosa, o un error de tipo `ButacaError` si ocurre un problema.
     */
    override fun cargarButacas(): Result<Unit, ButacaError> {
        logger.debug { "Importando datos de butacas" }
        val url = ClassLoader.getSystemResource(config.butacaSampleFile)
        if (url != null){
            butacaStorageIn
                .cargar(File(url.toURI()))
                .onSuccess {
                    it.forEach {
                        butacaValidator.validate(it).onSuccess {
                            butacaRepositorio.save(it)
                            logger.debug { "Añadida la butaca con id: ${it.id}" }
                        }.onFailure { logger.debug { it.message } }
                    }
                }
        }else return Err(ButacaError.ButacaStorageError("No se pudo leer el fichero correctamente"))
        return Ok(Unit)
    }

}