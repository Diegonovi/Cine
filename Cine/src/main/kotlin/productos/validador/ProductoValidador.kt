package org.example.productos.validador

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.productos.errors.ProductoError
import org.example.productos.models.Producto
import org.koin.core.annotation.Singleton

/**
 * Clase encargada de validar la integridad de los objetos Producto.
 */
@Singleton
class ProductoValidador (){
    /**
     * Valida un producto.
     * @param producto El producto a validar.
     * @return Un resultado que contiene el producto validado si pasa todas las validaciones, o un error si alguna validación falla.
     */
    fun validate(producto: Producto) : Result<Producto, ProductoError>{
        return when{
            producto.tipo == null -> Err(ProductoError.ProductoInvalido("Categoria inválida"))
            producto.stock < 0 -> Err(ProductoError.ProductoInvalido("El stock no puede ser menos de 0"))
            producto.precio < 0 -> Err(ProductoError.ProductoInvalido("El precio no puede ser menos de 0"))
            else -> Ok(producto)
        }
    }
}