package com.example.tosh.data

import com.example.tosh.domain.App
import com.example.tosh.domain.AppsRepository
import javax.inject.Inject

class AppsRepositoryImpl @Inject constructor(
    private val mapper: AppMapper,
) : AppsRepository {
    override fun getApps(): List<App> = hardcodedApps.map(mapper::toDomain)
}
