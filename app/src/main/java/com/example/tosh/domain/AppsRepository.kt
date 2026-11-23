package com.example.tosh.domain

interface AppsRepository {
    suspend fun getApps(): List<App>

    suspend fun getAppDetails(id: String): AppDetails

    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float>

    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float>
}
