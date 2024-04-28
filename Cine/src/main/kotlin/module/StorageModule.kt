package org.example.module

import org.example.butacas.repositorio.ButacaRepositorioImpl
import org.example.butacas.servicios.ButacaServiceImpl
import org.example.butacas.storage.ButacaStorageCSVImpl
import org.example.butacas.storage.ButacaStorageJSONImpl
import org.example.butacas.validator.ButacaValidator
import org.example.config.Config
import org.example.productos.repositorio.ProductoRepositorioImpl
import org.example.productos.servicio.ProductoServicioImpl
import org.example.productos.storage.ProductoStorageCSVImpl
import org.example.productos.validador.ProductoValidador
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Módulo de inyección de dependencias para la gestión del almacenamiento.
 */
public val storageModule : Module = module {
    single() {
        ButacaServiceImpl(
            butacaRepositorio = get(ButacaRepositorioImpl::class),
            butacaStorageOut = get(ButacaStorageJSONImpl::class),
            config = get(Config::class),
            butacaStorageIn = get(ButacaStorageCSVImpl::class),
            butacaValidator = get(ButacaValidator::class)
        )
    } bind(org.example.butacas.servicios.ButacaService::class)

    single() {
        ProductoServicioImpl(
            productosRepositorio = get(ProductoRepositorioImpl::class),
            productoValidador = get(ProductoValidador::class),
            productoStorage = get(ProductoStorageCSVImpl::class),
            config = get(Config::class)
        )
    } bind(org.example.productos.servicio.ProductoServicio::class)
}

