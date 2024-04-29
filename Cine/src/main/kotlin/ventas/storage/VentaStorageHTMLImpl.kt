package org.example.ventas.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.butacas.errors.ButacaError
import org.example.database.manager.logger
import org.example.ventas.errors.VentaError
import org.example.ventas.models.Venta
import org.koin.core.annotation.Singleton
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Implementación del almacenamiento de ventas desde y hacia archivos HTML.
 */
@Singleton
class VentaStorageHTMLImpl : VentaStorage {

    init {
        Files.createDirectories(Paths.get("data","ventas"))
    }

    override fun cargar(file: File): Result<List<Venta>, ButacaError>{
        println("No está implementada")
        return Ok(emptyList())
    }

    /**
     * Carga productos desde un archivo CSV.
     * @param venta la venta que quieres guardar.
     * @return Un resultado que contiene un Unit si lo pudo hacer bien o un error si hubo algún problema durante el procesoa.
     */
    override fun exportar(venta : Venta) : Result<Unit,VentaError>{
        try {
            val output = """
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Recibo</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
      }
      .receipt {
        max-width: 600px;
        border: 1px solid #ccc;
        padding: 20px;
        border-radius: 5px;
        background-color: #f9f9f9;
      }
      .receipt h2 {
        text-align: center;
      }
      .receipt p {
        margin: 5px 0;
      }
    </style>
  </head>
  <body>
    <div class="receipt">
      <h2>Recibo</h2>
      <p><strong>Cliente:</strong> ${venta.cliente.id}</p>
      <p><strong>Butaca:</strong> ${venta.butaca.id}</p>
      <p><strong>Complementos:</strong></p>
      <ul>
           ${getProductList(venta)}
      </ul>
      <p><strong>Precio Total:</strong> ${getPrice(venta)}€</p>
    </div>
  </body>
</html>
"""
            val date = venta.updatedAt
            val file = File("data${File.separator}ventas","entrada_${venta.butaca.id}_${venta.cliente.id}_${date.dayOfMonth}-${date.monthValue}-${date.year} ${date.hour}_${date.minute}_${date.second}.html")
            file.createNewFile()
            file.writeText(output)
            return Ok(Unit)
        }catch (e : Exception){
            logger.error { "Hubo un fallo al generar su recibo" }
            return Err(VentaError.VentaStorageError("Hubo un fallo al generar su recibo de la venta con id: ${venta.id}"))
        }
    }

    private fun getPrice(venta: Venta): String {
        var price = 0.0
        venta.lineasVenta.forEach {
            price += (it.precio * it.cantidad)
        }
        price  += venta.butaca.tipo!!.precio
        return price.toString()
    }

    private fun getProductList(venta: Venta) : String{
        var output = ""
        venta.lineasVenta.forEach {
            output += "      <li>${it.producto.nombre} \$ ${it.producto.precio} x ${it.cantidad} </li>"
        }
        if (output.isBlank()) return "<li>No se compraron complementos</li>"
        else return output
    }

}