package com.example.tosh.domain

interface AppsRepository {
    fun getApps(): List<App>
}
