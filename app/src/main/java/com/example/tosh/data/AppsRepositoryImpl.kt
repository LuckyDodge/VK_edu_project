package com.example.tosh.data

import com.example.tosh.domain.App
import com.example.tosh.domain.AppsRepository

class AppsRepositoryImpl(
    private val mapper: AppMapper = AppMapper(),
) : AppsRepository {
    override fun getApps(): List<App> = hardcodedApps.map(mapper::toDomain)
}
