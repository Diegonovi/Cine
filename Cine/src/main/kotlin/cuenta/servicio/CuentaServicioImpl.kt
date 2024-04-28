package org.example.cuenta.servicio

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import cuenta.errors.CuentaError
import org.example.cuenta.models.Cuenta
import org.example.cuenta.repositorio.CuentaRepositorio
import org.koin.core.annotation.Singleton

/**
 * Implementación de [CuentaServicio] que interactúa con el repositorio de cuentas para realizar operaciones.
 * @property cuentaRepositorio El repositorio de cuentas utilizado para acceder y manipular datos de cuentas.
 */
@Singleton
class CuentaServicioImpl(
    private var cuentaRepositorio: CuentaRepositorio,
): CuentaServicio {

    /**
     * Recupera todas las cuentas de usuario almacenadas.
     * @return un [Result] que contiene una lista de todas las cuentas de usuario o un error [CuentaError].
     */
    override fun findAll(): Result<List<Cuenta>, CuentaError> {
        return Ok(cuentaRepositorio.findAll())
    }

    /**
     * Busca una cuenta de usuario por su identificador único.
     * @param id El identificador único de la cuenta de usuario a buscar.
     * @return un [Result] que contiene la cuenta de usuario encontrada o un error [CuentaError].
     */
    override fun findById(id: String): Result<Cuenta, CuentaError> {
        val cuenta = cuentaRepositorio.findById(id)
        return if (cuenta != null) {
            Ok(cuenta)
        } else {
            Err(CuentaError.CuentaNotFoundError("La cuenta con ID $id no existe"))
        }
    }

    /**
     * Guarda una nueva cuenta de usuario.
     * @param cuenta La cuenta de usuario a guardar.
     * @return un [Result] que contiene la cuenta de usuario guardada o un error [CuentaError].
     */
    override fun save(cuenta: Cuenta): Result<Cuenta, CuentaError> {
        cuentaRepositorio.save(cuenta)?.let {
            return Ok(it)
        }
        return Err(CuentaError.CuentaStorageError("La cuenta con id: ${cuenta.id} no se pudo guardar"))
    }


}