package org.example.butacas.storage

import com.github.michaelbull.result.Result
import org.example.butacas.errors.ButacaError
import org.example.butacas.models.Butaca
import java.io.File

interface ButacaStorage {
    fun cargar(file : File) : Result<List<Butaca>,ButacaError>
    fun exportar(list: List<Butaca>) : Result<Unit,ButacaError>
}