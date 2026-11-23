package com.example.tosh.data

import com.example.tosh.domain.App
import com.example.tosh.domain.AppDetails
import javax.inject.Inject

class AppMapper @Inject constructor() {
    fun toDomain(dto: AppDto): App = App(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        category = dto.category,
        iconUrl = dto.iconUrl,
    )

    fun toDetails(dto: AppDetailsDto): AppDetails = AppDetails(
        id = dto.id,
        name = dto.name,
        developer = dto.developer,
        category = dto.category,
        ageRating = dto.ageRating,
        size = dto.size.toFloat(),
        iconUrl = dto.iconUrl,
        screenshotUrlList = dto.screenshotUrlList,
        description = dto.description,
    )

    fun toDto(app: App): AppDto = AppDto(
        id = app.id,
        name = app.name,
        description = app.description,
        category = app.category,
        iconUrl = app.iconUrl,
    )

    fun toDetailsDto(details: AppDetails): AppDetailsDto = AppDetailsDto(
        id = details.id,
        name = details.name,
        developer = details.developer,
        category = details.category,
        ageRating = details.ageRating,
        size = details.size.toDouble(),
        iconUrl = details.iconUrl,
        screenshotUrlList = details.screenshotUrlList,
        description = details.description,
    )
}
