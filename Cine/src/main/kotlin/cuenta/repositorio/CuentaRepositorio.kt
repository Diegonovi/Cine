package org.example.cuenta.repositorio

import org.example.cuenta.models.Cuenta

interface CuentaRepositorio {
    fun findAll(): List<Cuenta>
    fun findById(id: String): Cuenta?
    fun save(cuenta: Cuenta): Cuenta?
    fun update(id: String, cuenta: Cuenta): Cuenta?
    fun delete(id: String): Cuenta?
}