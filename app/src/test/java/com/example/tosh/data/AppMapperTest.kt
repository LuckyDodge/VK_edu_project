package com.example.tosh.data

import com.example.tosh.domain.App
import com.example.tosh.domain.AppDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class AppMapperTest {
    private val mapper = AppMapper()

    @Test
    fun toDomainCopiesCatalogAndLeavesRatingEmpty() {
        val dto = AppDto(
            id = "1",
            name = "QuickNote",
            description = "Короткие заметки",
            category = "Продуктивность",
            iconUrl = "https://example.com/icon.png",
        )

        assertEquals(
            App(
                id = "1",
                name = "QuickNote",
                description = "Короткие заметки",
                category = "Продуктивность",
                iconUrl = "https://example.com/icon.png",
            ),
            mapper.toDomain(dto),
        )
    }

    @Test
    fun toDtoDropsRating() {
        val app = App(
            id = "1",
            name = "QuickNote",
            description = "Короткие заметки",
            category = "Продуктивность",
            iconUrl = "https://example.com/icon.png",
            rating = 4.2f,
        )

        assertEquals(
            AppDto(
                id = "1",
                name = "QuickNote",
                description = "Короткие заметки",
                category = "Продуктивность",
                iconUrl = "https://example.com/icon.png",
            ),
            mapper.toDto(app),
        )
    }

    @Test
    fun toDetailsConvertsSizeAndKeepsScreenshots() {
        val dto = AppDetailsDto(
            id = "204a939c",
            name = "GymMate",
            developer = "Fit Lab",
            category = "Здоровье и фитнес",
            ageRating = 12,
            size = 24.6,
            iconUrl = "https://example.com/gym.png",
            screenshotUrlList = listOf("https://example.com/1.png", "https://example.com/2.png"),
            description = "Тренировки дома",
        )

        assertEquals(
            AppDetails(
                id = "204a939c",
                name = "GymMate",
                developer = "Fit Lab",
                category = "Здоровье и фитнес",
                ageRating = 12,
                size = 24.6f,
                iconUrl = "https://example.com/gym.png",
                screenshotUrlList = listOf("https://example.com/1.png", "https://example.com/2.png"),
                description = "Тренировки дома",
            ),
            mapper.toDetails(dto),
        )
    }

    @Test
    fun toDetailsDtoConvertsSizeBack() {
        val details = AppDetails(
            id = "204a939c",
            name = "GymMate",
            developer = "Fit Lab",
            category = "Здоровье и фитнес",
            ageRating = 12,
            size = 24.6f,
            iconUrl = "https://example.com/gym.png",
            screenshotUrlList = listOf("https://example.com/1.png"),
            description = "Тренировки дома",
        )

        assertEquals(
            AppDetailsDto(
                id = "204a939c",
                name = "GymMate",
                developer = "Fit Lab",
                category = "Здоровье и фитнес",
                ageRating = 12,
                size = 24.6f.toDouble(),
                iconUrl = "https://example.com/gym.png",
                screenshotUrlList = listOf("https://example.com/1.png"),
                description = "Тренировки дома",
            ),
            mapper.toDetailsDto(details),
        )
    }

    @Test
    fun detailsRoundTripKeepsDomain() {
        val details = AppDetails(
            id = "204a939c",
            name = "GymMate",
            developer = "Fit Lab",
            category = "Здоровье и фитнес",
            ageRating = 12,
            size = 24.6f,
            iconUrl = "https://example.com/gym.png",
            screenshotUrlList = emptyList(),
            description = "Тренировки дома",
        )

        assertEquals(details, mapper.toDetails(mapper.toDetailsDto(details)))
    }
}
