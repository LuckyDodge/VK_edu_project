package com.example.tosh.data

import kotlinx.coroutines.delay
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(): List<AppDto>

    @GET("catalog/{id}")
    suspend fun getApp(@Path("id") id: String): AppDetailsDto

    suspend fun getRating(id: String): Float {
        delay(1500)
        return (30..50).random() / 10f
    }
}
