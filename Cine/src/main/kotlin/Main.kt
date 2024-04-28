package org.example

import cine.app.CineApp
import org.example.butacas.repositorio.ButacaRepositorio
import org.example.butacas.servicios.ButacaService
import org.example.butacas.storage.ButacaStorage
import org.example.butacas.storage.ProductoStorage
import org.example.butacas.validator.ButacaValidator
import org.example.config.Config
import org.example.cuenta.repositorio.CuentaRepositorio
import org.example.database.manager.SqlDelightManager
import org.example.module.storageModule
import org.example.productos.repositorio.ProductosRepositorio
import org.example.productos.validador.ProductoValidador
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import org.koin.test.verify.verify

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@OptIn(KoinExperimentalAPI::class)
fun main() {
    startKoin{
        printLogger()
        // Leemos las propiedades de un fichero
        fileProperties("/config.properties") // Por defecto busca en src/main/resources/config.properties, pero puede ser otro fichero si se lo pasas como parametro
        // declara modulos de inyección de dependencias, pero lo verificamos antes de inyectarlos
        // para asegurarnos que todo está correcto y no nos de errores
        storageModule.verify(
            extraTypes = listOf(
                Boolean::class,
                Int::class,
                ButacaRepositorio::class,
                ButacaStorage::class,
                ButacaValidator::class,
                CuentaRepositorio::class,
                ProductosRepositorio::class,
                ProductoValidador::class,
                ProductoStorage::class,
                Config::class
            )
        )
        defaultModule.verify(
            extraTypes = listOf(
                Boolean::class,
                Int::class,
                SqlDelightManager::class,
                ButacaService::class
            )
        )
        modules(storageModule, defaultModule)
    }
    val cine = CineApp()
    cine.iniciarCine()
}

