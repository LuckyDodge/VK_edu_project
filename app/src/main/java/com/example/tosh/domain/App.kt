package com.example.tosh.domain

data class App(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val developer: String,
    val ageRating: Int,
    val sizeMb: Int,
    val iconText: String,
    val iconColor: Int,
)
