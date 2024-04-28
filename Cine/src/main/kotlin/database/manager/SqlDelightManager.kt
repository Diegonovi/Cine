package org.example.database.manager

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.DatabaseQueries
import org.cine.database.AppDatabase
import org.example.config.Config
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging

val logger = logging()

@Singleton
class SqlDelightManager(
    private val config : Config
) {
    private val databaseUrl: String = config.databaseUrl
    private val databaseInitData: Boolean = config.databaseInitData
    private val databaseInMemory: Boolean = config.databaseInMemory
    val databaseQueries: DatabaseQueries = initQueries()

    init {
        logger.debug { "Inicializando el gestor de Bases de Datos con SQLDelight" }
        initialize()
    }

    /**
     * Crea la base de datos en memoria o en fichero dependiendo de lo que ponga en
     * @return un objeto DatabaseQueries que es utilizado por otras clases para
     * utilizar las funciones creadas automaticamente por SQLDelight a partir de las
     * que están presentes en el fichero Database.sq
     */
    private fun initQueries(): DatabaseQueries {

        return if (databaseInMemory) {
            logger.debug { "SqlDeLightClient - InMemory" }
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        } else {
            logger.debug { "SqlDeLightClient - File: ${databaseUrl}" }
            JdbcSqliteDriver(databaseUrl)
        }.let { driver ->
            // Creamos la base de datos
            logger.debug { "Creando Tablas (si es necesario)" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.databaseQueries

    }

    /**
     * Borra todos los datos existentes en la base datos y carga los de ejemplo
     * @see removeAllData
     */
    fun initialize() {
        if (databaseInitData) {
            removeAllData()
        }
    }


    /**
     * Borra todos los datos de la base de datos con la ayuda de las funciones que crea
     * SqlDelight dentro del fichero Database.sq
     */
    private fun removeAllData() {
        logger.debug { "Borrando todo el data existente en la base de datos" }
        databaseQueries.transaction {
            databaseQueries.removeAllButacas()
            databaseQueries.removeAllCuentas()
            databaseQueries.removeAllProductos()
            databaseQueries.removeAllVentas()
        }
    }
}