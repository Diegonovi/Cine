package org.example.config

import org.example.database.manager.logger
import org.koin.core.annotation.Property
import org.koin.core.annotation.Singleton
import java.nio.file.Files
import kotlin.io.path.Path

/**
 * Configura las opciones de la aplicación.
 *
 * @param _databaseUrl La URL de la base de datos a utilizar. Por defecto es "jdbc:sqlite:productos.db".
 * @param _databaseInitTables Indica si se deben inicializar las tablas de la base de datos. Por defecto es "true".
 * @param _databaseInitData Indica si se debe inicializar el contenido de la base de datos. Por defecto es "true".
 * @param _databaseInMemory Indica si se debe utilizar una base de datos en memoria. Por defecto es "true".
 * @param _storageData La carpeta donde se almacenará el contenido. Por defecto es "data".
 * @param _butacaSampleFile El archivo de muestra de datos de butacas. Por defecto es "butacas.csv".
 * @param _productoImportFile El archivo que contiene los datos de los productos a importar. Por defecto es "productos.csv".
 */
@Singleton
class Config(
    @Property("database.url")
    _databaseUrl: String = "jdbc:sqlite:productos.db",
    @Property("database.init.tables")
    _databaseInitTables: String = "true",
    @Property("database.init.data")
    _databaseInitData: String = "true",
    @Property("database.inmemory")
    _databaseInMemory: String = "true",
    @Property("storage.data")
    _storageData: String = "data",
    @Property("butacas.butacaSampleFile")
    _butacaSampleFile : String = "butacas.csv",
    @Property("productos.productoImportFile")
    _productoImportFile : String = "productos.csv"
) {
    val databaseUrl: String = _databaseUrl
    val databaseInitTables: Boolean = _databaseInitTables.toBoolean()
    val databaseInitData: Boolean = _databaseInitData.toBoolean()
    val databaseInMemory: Boolean = _databaseInMemory.toBoolean()
    val storageData: String = _storageData
    val butacaSampleFile : String = _butacaSampleFile
    val productoImportFile : String = _productoImportFile

    init {
        try {
            logger.debug { "Cargando configuración" }
            // crear el directorio si no existe
            Files.createDirectories(Path(storageData))

        } catch (e: Exception) {
            logger.error { "Error cargando configuración: ${e.message}" }
        }

    }
}