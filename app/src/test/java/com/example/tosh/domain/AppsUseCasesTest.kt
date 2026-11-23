package com.example.tosh.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class AppsUseCasesTest {
    @Test
    fun getAppsReturnsRepositoryList() = runBlocking {
        val apps = listOf(app("1", rating = 4.5f), app("2"))

        assertEquals(apps, GetAppsUseCase(FakeAppsRepository(apps = apps))())
    }

    @Test
    fun getAppsReturnsEmptyList() = runBlocking {
        assertEquals(emptyList<App>(), GetAppsUseCase(FakeAppsRepository())())
    }

    @Test
    fun loadRatingsSequentiallyPassesIds() = runBlocking {
        val repository = FakeAppsRepository(ratings = mapOf("1" to 4.5f, "2" to 3.0f))

        val ratings = LoadRatingsSequentiallyUseCase(repository)(listOf("1", "2"))

        assertEquals(listOf("1", "2"), repository.sequentialIds)
        assertNull(repository.parallelIds)
        assertEquals(mapOf("1" to 4.5f, "2" to 3.0f), ratings)
    }

    @Test
    fun loadRatingsInParallelPassesIds() = runBlocking {
        val repository = FakeAppsRepository(ratings = mapOf("1" to 4.5f, "2" to 3.0f))

        val ratings = LoadRatingsInParallelUseCase(repository)(listOf("1", "2"))

        assertEquals(listOf("1", "2"), repository.parallelIds)
        assertNull(repository.sequentialIds)
        assertEquals(mapOf("1" to 4.5f, "2" to 3.0f), ratings)
    }

    @Test
    fun getAppsPropagatesRepositoryError() {
        val repository = FakeAppsRepository(error = IllegalStateException("network"))

        val error = assertThrows(IllegalStateException::class.java) {
            runBlocking { GetAppsUseCase(repository)() }
        }

        assertEquals("network", error.message)
    }

    private fun app(id: String, rating: Float? = null) = App(
        id = id,
        name = "App $id",
        description = "Описание",
        category = "Продуктивность",
        iconUrl = "https://example.com/$id.png",
        rating = rating,
    )

    private class FakeAppsRepository(
        private val apps: List<App> = emptyList(),
        private val ratings: Map<String, Float> = emptyMap(),
        private val error: Throwable? = null,
    ) : AppsRepository {
        var sequentialIds: List<String>? = null
        var parallelIds: List<String>? = null

        override suspend fun getApps(): List<App> {
            error?.let { throw it }
            return apps
        }

        override suspend fun getAppDetails(id: String): AppDetails = error(id)

        override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
            sequentialIds = ids
            return ratings
        }

        override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> {
            parallelIds = ids
            return ratings
        }
    }
}
