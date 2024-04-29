package org.example.butacas.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.butacas.errors.ButacaError
import org.example.butacas.mappers.elegirEstado
import org.example.butacas.mappers.elegirOcupamiento
import org.example.butacas.mappers.elegirTipo
import org.example.butacas.models.Butaca
import org.example.butacas.validator.ButacaValidator
import org.example.database.manager.logger
import org.koin.core.annotation.Singleton
import java.io.File
import java.time.LocalDateTime

/**
 * Implementación de [ButacaStorage] utilizando archivos CSV.
 *
 * @param butacaValidator Instancia de [ButacaValidator] para validar las instancias de [Butaca].
 */
@Singleton
class ButacaStorageCSVImpl(
    var butacaValidator: ButacaValidator
) : ButacaStorage {

    /**
     * Exporta una lista de Butacas instancias a un archivo CSV.
     *
     * @param list La lista de [Butaca instancias a ser exportadas.
     * @return Un Result que contiene Unit si la exportación fue exitosa, o un ButacaError si ocurrió un error.
     */
    override fun exportar(list: List<Butaca>): Result<Unit,ButacaError> {
        println("Una función para exportar una lista de butacas en CSV")
        return Err(ButacaError.ButacaStorageError("Esta función no está implementada"))
    }

    /**
     * Carga una lista de Butaca instancias desde un archivo CSV.
     *
     * @param file El File que contiene los datos del archivo CSV.
     * @return Un Result que contiene la lista de Butaca instancias si la carga fue exitosa, o un ButacaError si ocurrió un error.
     */
    override fun cargar(file: File) : Result<List<Butaca>, ButacaError>{
        try {
            return Ok(
                file.readLines()
                    .drop(1)
                    .map {
                    val butaca = it.split(',')
                    Butaca(
                        id = butaca[0],
                        estado = elegirEstado(butaca[1]),
                        ocupamiento = elegirOcupamiento(butaca[2]),
                        tipo = elegirTipo(butaca[3]),
                        isDeleted = false,
                        updatedAt = LocalDateTime.now(),
                        createdAt = LocalDateTime.now()
                    )
                }
            )
        }catch (e : Exception){
            logger.debug { "Hubo un error al cargar las butacas del archivo ${file.name}" }
            return Err(ButacaError.ButacaStorageError("Hubo un error al cargar las butacas del archivo ${file.name}"))
        }
    }
}