package org.example.butacas.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.butacas.dto.ButacaDto
import org.example.butacas.errors.ButacaError
import org.example.butacas.mappers.toButacaDTO
import org.example.butacas.models.Butaca
import org.koin.core.annotation.Singleton
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

/**
 * Implementación de [ButacaStorage] utilizando JSON para almacenar y exportar los datos de [Butaca].
 *
 * @see ButacaStorage
 */
@Singleton
class ButacaStorageJSONImpl : ButacaStorage {
    /**
     * Inicializa la storage creando los directorios necesarios.
     */
    init {
        Files.createDirectories(Paths.get("data","butacas"))
    }

    /**
     * Carga los datos de Butaca desde un archivo JSON.
     *
     * @param file El archivo que contiene los datos del archivo JSON.
     * @return Un Result que contiene los datos de Butaca cargados o un error si el archivo no puede leerse o parsearse.
     * @see Result
     */
    override fun cargar(file: File): Result<List<Butaca>, ButacaError> {
        return Err(ButacaError.ButacaStorageError("Esta funcion no esta implementada"))
        /*No está implementado*/
    }

    /**
     * Exporta los datos de Butaca a un archivo JSON.
     *
     * @param list La lista de datos de Butaca a ser exportados.
     * @return Un Result que contiene el resultado de la exportación o un error si no se puede crear o escribir el archivo.
     * @see Result
     */
    override fun exportar(list: List<Butaca>): Result<Unit,ButacaError> {
        val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
        val dateTime = LocalDateTime.now()
        println()
            var file = File("data${File.separator}butacas","butacas${dateTime.dayOfMonth}-${dateTime.monthValue}-${dateTime.year} ${dateTime.hour}_${dateTime.minute}_${dateTime.second}.json")
            file.createNewFile()
            val butacasDTO = list.map {
                it.toButacaDTO()
            }
            file.writeText(json.encodeToString<List<ButacaDto>>(butacasDTO))
        return Ok(Unit)
    }
}