package com.example.tosh.data

import com.example.tosh.domain.App
import com.example.tosh.domain.AppDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AppsRepositoryImplTest {
    @Test
    fun getAppsMapsCatalogInOrder() = runBlocking {
        val repository = AppsRepositoryImpl(
            FakeCatalogApi(
                catalog = listOf(
                    catalogDto("1", "QuickNote"),
                    catalogDto("2", "GymMate"),
                ),
            ),
            AppMapper(),
        )

        assertEquals(
            listOf(
                catalogApp("1", "QuickNote"),
                catalogApp("2", "GymMate"),
            ),
            repository.getApps(),
        )
    }

    @Test
    fun getAppsReturnsEmptyList() = runBlocking {
        val repository = AppsRepositoryImpl(FakeCatalogApi(), AppMapper())

        assertEquals(emptyList<App>(), repository.getApps())
    }

    @Test
    fun getAppDetailsUsesRequestedId() = runBlocking {
        val api = FakeCatalogApi(
            details = mapOf(
                "gym" to detailsDto("gym"),
                "other" to detailsDto("other"),
            ),
        )
        val repository = AppsRepositoryImpl(api, AppMapper())

        val details = repository.getAppDetails("gym")

        assertEquals(listOf("gym"), api.requestedDetailIds)
        assertEquals(detailsApp("gym"), details)
    }

    @Test
    fun loadRatingsSequentiallyDoesNotOverlap() = runBlocking {
        val api = FakeCatalogApi(
            ratings = mapOf("1" to 3.5f, "2" to 4.0f, "3" to 5.0f),
            ratingDelayMillis = 40,
        )
        val repository = AppsRepositoryImpl(api, AppMapper())

        val ratings = repository.loadRatingsSequentially(listOf("1", "2", "3"))

        assertEquals(mapOf("1" to 3.5f, "2" to 4.0f, "3" to 5.0f), ratings)
        assertEquals(1, api.maxInFlight)
    }

    @Test
    fun loadRatingsInParallelOverlaps() = runBlocking {
        val api = FakeCatalogApi(
            ratings = mapOf("1" to 3.5f, "2" to 4.0f, "3" to 5.0f),
            ratingDelayMillis = 40,
        )
        val repository = AppsRepositoryImpl(api, AppMapper())

        val ratings = repository.loadRatingsInParallel(listOf("1", "2", "3"))

        assertEquals(mapOf("1" to 3.5f, "2" to 4.0f, "3" to 5.0f), ratings)
        assertEquals(3, api.maxInFlight)
    }

    private fun catalogDto(id: String, name: String) = AppDto(
        id = id,
        name = name,
        description = "Описание",
        category = "Продуктивность",
        iconUrl = "https://example.com/$id.png",
    )

    private fun catalogApp(id: String, name: String) = App(
        id = id,
        name = name,
        description = "Описание",
        category = "Продуктивность",
        iconUrl = "https://example.com/$id.png",
    )

    private fun detailsDto(id: String) = AppDetailsDto(
        id = id,
        name = "GymMate",
        developer = "Fit Lab",
        category = "Здоровье и фитнес",
        ageRating = 12,
        size = 24.6,
        iconUrl = "https://example.com/$id.png",
        screenshotUrlList = listOf("https://example.com/shot.png"),
        description = "Тренировки дома",
    )

    private fun detailsApp(id: String) = AppDetails(
        id = id,
        name = "GymMate",
        developer = "Fit Lab",
        category = "Здоровье и фитнес",
        ageRating = 12,
        size = 24.6f,
        iconUrl = "https://example.com/$id.png",
        screenshotUrlList = listOf("https://example.com/shot.png"),
        description = "Тренировки дома",
    )

    private class FakeCatalogApi(
        private val catalog: List<AppDto> = emptyList(),
        private val details: Map<String, AppDetailsDto> = emptyMap(),
        private val ratings: Map<String, Float> = emptyMap(),
        private val ratingDelayMillis: Long = 0,
    ) : CatalogApi {
        val requestedDetailIds = mutableListOf<String>()
        var maxInFlight = 0
        private var inFlight = 0

        override suspend fun getCatalog(): List<AppDto> = catalog

        override suspend fun getApp(id: String): AppDetailsDto {
            requestedDetailIds += id
            return details.getValue(id)
        }

        override suspend fun getRating(id: String): Float {
            inFlight += 1
            maxInFlight = maxOf(maxInFlight, inFlight)
            try {
                if (ratingDelayMillis > 0) delay(ratingDelayMillis)
            } finally {
                inFlight -= 1
            }
            return ratings.getValue(id)
        }
    }
}
