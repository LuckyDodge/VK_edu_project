package com.example.tosh.data

import com.example.tosh.domain.App

class AppMapper {
    fun toDomain(dto: AppDto): App = App(
        id = dto.id,
        name = dto.title,
        description = dto.shortDescription,
        category = dto.category,
        developer = dto.developer,
        ageRating = dto.ageRating,
        sizeMb = dto.size.toInt(),
        iconText = dto.iconLabel,
        iconColor = dto.iconColor.toInt(),
    )
}
