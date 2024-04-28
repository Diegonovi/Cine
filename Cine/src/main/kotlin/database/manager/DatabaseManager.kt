package org.example.database.manager

import org.apache.ibatis.jdbc.ScriptRunner
import org.example.config.Config
import org.koin.core.annotation.Singleton
import java.io.PrintWriter
import java.io.Reader
import java.sql.Connection
import java.sql.DriverManager

@Singleton
class DataBaseManager(
    private val config: Config
) : AutoCloseable {
    var connection: Connection? = null
        private set

    /**
     * Inicializamos la base de datos
     */
    init {
        // Iniciamos la base de datos
        initConexion()
    }

    /**
     * Inicializamos la conexión con la base de datos
     */

    private fun initConexion() {
        // Inicializamos la base de datos
        logger.debug { "Iniciando conexión con la base de datos" }
        if (connection == null || connection!!.isClosed) {
            connection = DriverManager.getConnection(config.databaseUrl)
        }
        logger.debug { "Conexión con la base de datos inicializada" }
    }


    /**
     * Cerramos la conexión con la base de datos
     */
    override fun close() {
        logger.debug { "Cerrando conexión con la base de datos" }
        if (!connection!!.isClosed) {
            connection!!.close()
        }
        logger.debug { "Conexión con la base de datos cerrada" }
    }

    /**
     * Ejecuta una función que utiliza la base de datos y la cierra al finalizar.
     * @param block La función que utiliza la base de datos.
     */
    fun <T> use(block: (DataBaseManager) -> T) {
        try {
            initConexion()
            block(this)
        } catch (e: Exception) {
            logger.error { "Error en la base de datos: ${e.message}" }
        } finally {
            close()
        }
    }

    /**
     * Ejecuta un script SQL en la base de datos.
     * @param reader El lector que contiene el script SQL.
     * @param logWriter Indica si se debe escribir el log de la ejecución del script.
     */
    private fun scriptRunner(reader: Reader, logWriter: Boolean = false) {
        logger.debug { "Ejecutando script SQL con log: $logWriter" }
        val sr = ScriptRunner(connection)
        sr.setLogWriter(if (logWriter) PrintWriter(System.out) else null)
        sr.runScript(reader)
    }
}