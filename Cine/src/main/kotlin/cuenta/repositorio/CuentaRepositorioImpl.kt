package org.example.cuenta.repositorio

import org.example.cuenta.dto.CuentaDTO
import org.example.cuenta.mappers.toCuenta
import org.example.cuenta.mappers.toCuentaDto
import org.example.cuenta.models.Cuenta
import org.example.database.manager.DataBaseManager
import org.example.database.manager.logger
import org.koin.core.annotation.Singleton
import java.time.LocalDateTime

@Singleton
class CuentaRepositorioImpl(
    val dataBaseManager: DataBaseManager
) : CuentaRepositorio {

    /**
     * Recupera todas las cuentas de usuario almacenadas en la base de datos.
     * @return una lista de todas las cuentas de usuario, o una lista vacía si no se encontraron cuentas o si ocurrió un error.
     */
    override fun findAll(): List<Cuenta> {
        logger.debug { "Buscando todas las personas" }
        try {
            val personas = mutableListOf<Cuenta>()
            dataBaseManager.use { db ->
                val sql = "SELECT * FROM CuentaEntity WHERE updatedAt = (SELECT MAX(updatedAt) FROM CuentaEntity AS b2 WHERE b2.id = CuentaEntity.id)"
                val result = db.connection?.prepareStatement(sql)!!.executeQuery()
                while (result.next()) {
                    val persona = CuentaDTO(
                        id = result.getString("id"),
                        isDeleted = result.getInt("isDeleted"),
                        createdAt = result.getString("createdAt"),
                        updatedAt = result.getString("updatedAt")
                    ).toCuenta()
                    personas.add(persona)
                }
            }
            return personas
        } catch (e: Exception) {
            logger.error { "Hubo un error al cargar las cuentas" }
            return emptyList()
        }
    }

    /**
     * Busca una cuenta de usuario por su identificador único.
     * @param id El identificador único de la cuenta de usuario a buscar.
     * @return la cuenta de usuario encontrada, o null si no se encontró ninguna cuenta con el identificador proporcionado o si ocurrió un error.
     */
    override fun findById(id: String): Cuenta? {
        logger.debug { "Buscando cliente por id $id" }
        try {
            var cuenta: Cuenta? = null
            dataBaseManager.use { db ->
                val sql = "SELECT * FROM CuentaEntity WHERE updatedAt = (SELECT MAX(updatedAt) FROM CuentaEntity AS b2 WHERE b2.id = CuentaEntity.id) AND id = ?";
                val statement = db.connection?.prepareStatement(sql)!!
                statement.setString(1, id)
                val result = statement.executeQuery()
                if (result.next()) {
                    cuenta = CuentaDTO(
                        id = result.getString("id"),
                        updatedAt = result.getString("updatedAt"),
                        createdAt = result.getString("createdAt"),
                        isDeleted = result.getInt("isDeleted")
                    ).toCuenta()
                }
            }
            return cuenta //Si no ha fallado
        } catch (e: Exception) {
            logger.error { "Error al encontrar la cuenta con id: $id" }
            return null //Si ha fallado
        }
    }

    /**
     * Guarda una nueva cuenta de usuario en la base de datos.
     * @param cuenta La cuenta de usuario a guardar.
     * @return la cuenta de usuario guardada, o null si la cuenta ya existe en la base de datos o si ocurrió un error.
     */
    override fun save(cuenta: Cuenta): Cuenta? {
        logger.debug { "Guardando cuenta con id: ${cuenta.id}" }
        try {
            if (findById(cuenta.id) == null){
                var cuentaDTO = cuenta.toCuentaDto()
                val timeStamp = LocalDateTime.now()
                dataBaseManager.use { db ->
                    val sql =
                        "INSERT INTO CuentaEntity (id, isDeleted, createdAt, updatedAt) VALUES (?, ?, ?, ?)"
                    val statement = db.connection?.prepareStatement(sql)!!
                    statement.setString(1, cuentaDTO.id)
                    statement.setInt(2, cuentaDTO.isDeleted)
                    statement.setString(3, timeStamp.toString())
                    statement.setString(4, timeStamp.toString())
                    statement.executeUpdate()
                }
                return cuenta //Si no existe y no falla
            }else return null //Si ya existe en la base de datos
        } catch (e: Exception) {
            logger.error { "Error al guardar la cuenta con id: ${cuenta.id}" }
            return null //Si falla
        }
    }

    /**
     * Actualiza una cuenta de usuario existente en la base de datos.
     * @param id El identificador único de la cuenta de usuario a actualizar.
     * @param cuenta La nueva información de la cuenta de usuario.
     * @return la cuenta de usuario actualizada, o null si la cuenta no existe en la base de datos o si ocurrió un error.
     */
    override fun update(id: String, cuenta: Cuenta): Cuenta? {
        logger.debug { "Actualizando cuenta con id: $id" }
        findById(id)?.let {
            try {
                if (findById(cuenta.id) == null){
                    var cuentaDTO = cuenta.toCuentaDto()
                    val timeStamp = LocalDateTime.now()
                    dataBaseManager.use { db ->
                        val sql =
                            "INSERT INTO CuentaEntity (id, isDeleted, createdAt, updatedAt) VALUES (?, ?, ?, ?)"
                        val statement = db.connection?.prepareStatement(sql)!!
                        statement.setString(1, cuentaDTO.id)
                        statement.setInt(2, cuentaDTO.isDeleted)
                        statement.setString(3, cuentaDTO.createdAt)
                        statement.setString(4, timeStamp.toString())
                        statement.executeUpdate()
                    }
                    return cuenta //Si no existe y no falla
                }else return null //Si ya existe en la base de datos
            } catch (e: Exception) {
                logger.error { "Error al actualizar la cuenta con id: $id" }
                return null //Si falla
            }
        }
        return null //Si no existe
    }

    /**
     * Elimina una cuenta de usuario de la base de datos.
     * @param id El identificador único de la cuenta de usuario a eliminar.
     * @return la cuenta de usuario eliminada, o null si la cuenta no existe en la base de datos o si ocurrió un error.
     */
    override fun delete(id: String): Cuenta? {
        logger.debug { "Borrando la cuenta con id $id" }
        findById(id)?.let {
            try {
                var cuentaDTO = it.toCuentaDto()
                val timeStamp = LocalDateTime.now()
                dataBaseManager.use { db ->
                    val sql =
                        "INSERT INTO CuentaEntity (id, isDeleted, createdAt, updatedAt) VALUES (?, ?, ?, ?)"
                    val statement = db.connection?.prepareStatement(sql)!!
                    statement.setString(1, cuentaDTO.id)
                    statement.setInt(2, 1)
                    statement.setString(3, cuentaDTO.createdAt)
                    statement.setString(4, timeStamp.toString())
                    statement.executeUpdate()
                }
                return it //Si existe y no ha fallado
            } catch (e: Exception) {
                logger.error { "Error al borrar la cuenta con id: $id" }
                return null //Si falla
            }
        }
        return null //Si no existe
    }
}