package com.example.tosh.data

data class AppDto(
    val id: String,
    val title: String,
    val shortDescription: String,
    val category: String,
    val developer: String,
    val ageRating: Int = 12,
    val size: Double = 100.0,
    val iconLabel: String,
    val iconColor: Long,
)
