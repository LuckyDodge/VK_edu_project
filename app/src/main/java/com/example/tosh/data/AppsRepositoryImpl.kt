package com.example.tosh.data

import com.example.tosh.domain.App
import com.example.tosh.domain.AppDetails
import com.example.tosh.domain.AppsRepository
import javax.inject.Inject

class AppsRepositoryImpl @Inject constructor(
    private val api: CatalogApi,
    private val mapper: AppMapper,
) : AppsRepository {
    override suspend fun getApps(): List<App> = api.getCatalog().map(mapper::toDomain)

    override suspend fun getAppDetails(id: String): AppDetails = mapper.toDetails(api.getApp(id))
}
