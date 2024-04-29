package org.example.productos.errors


/**
 * Clase sellada que representa los posibles errores relacionados con los productos.
 * @param message El mensaje de error asociado.
 */
sealed class ProductoError(val message: String) {

    // Error que indica un problema relacionado con el almacenamiento de productos.
    class ProductoStorageError(message: String) : ProductoError(message)

    // Error que indica un producto inválido.
    class ProductoInvalido(message: String) : ProductoError(message)

    // Error que indica que el producto no ha sido encontrado.
    class ProductoNotFoundError(message: String) : ProductoError(message)
}