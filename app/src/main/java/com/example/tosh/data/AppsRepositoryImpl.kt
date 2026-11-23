package com.example.tosh.data

import com.example.tosh.domain.App
import com.example.tosh.domain.AppDetails
import com.example.tosh.domain.AppsRepository
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class AppsRepositoryImpl @Inject constructor(
    private val api: CatalogApi,
    private val mapper: AppMapper,
) : AppsRepository {
    override suspend fun getApps(): List<App> = api.getCatalog().map(mapper::toDomain)

    override suspend fun getAppDetails(id: String): AppDetails = mapper.toDetails(api.getApp(id))

    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
        val ratings = mutableMapOf<String, Float>()
        for (id in ids) {
            ratings[id] = api.getRating(id)
        }
        return ratings
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id ->
            async { id to api.getRating(id) }
        }.awaitAll().toMap()
    }
}
