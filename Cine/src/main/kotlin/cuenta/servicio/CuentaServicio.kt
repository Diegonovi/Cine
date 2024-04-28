package org.example.cuenta.servicio

import com.github.michaelbull.result.Result
import cuenta.errors.CuentaError
import org.example.cuenta.models.Cuenta

interface CuentaServicio {
    fun findAll() : Result<List<Cuenta>, CuentaError>
    fun findById(id : String) : Result<Cuenta, CuentaError>
    fun save(cuenta: Cuenta) : Result<Cuenta,CuentaError>
}