package org.example.butacas.repositorio

import org.example.butacas.models.Butaca
import java.time.LocalDateTime

interface ButacaRepositorio {
    fun findAll(): List<Butaca>
    fun findAllBasedOnDate(date: LocalDateTime) : List<Butaca>
    fun findById(id: String): Butaca?
    fun save(butaca: Butaca, ignoreKey : Boolean = false): Butaca?
    fun update(id: String, butaca: Butaca): Butaca?
    fun delete(id: String): Butaca?
    fun findByIdAndDate(id : String, date: LocalDateTime) : Butaca?
}