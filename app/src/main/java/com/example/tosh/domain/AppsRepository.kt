package com.example.tosh.domain

interface AppsRepository {
    suspend fun getApps(): List<App>

    suspend fun getAppDetails(id: String): AppDetails
}
