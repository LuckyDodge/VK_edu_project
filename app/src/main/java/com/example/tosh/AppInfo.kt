package com.example.tosh

import androidx.annotation.ColorInt

data class AppInfo(
    val name: String,
    val description: String,
    val category: String,
    val iconText: String,
    @param:ColorInt val iconColor: Int,
)

val sampleApps = listOf(
    AppInfo(
        name = "VK Музыка",
        description = "Музыка и подкасты для любого настроения",
        category = "Музыка",
        iconText = "♫",
        iconColor = 0xFF7C4DFF.toInt(),
    ),
    AppInfo(
        name = "VK Видео",
        description = "Фильмы, сериалы, шоу и прямые трансляции",
        category = "Видео",
        iconText = "▶",
        iconColor = 0xFFFF5252.toInt(),
    ),
    AppInfo(
        name = "VK Мессенджер",
        description = "Общение с друзьями в личных и групповых чатах",
        category = "Социальные сети",
        iconText = "✉",
        iconColor = 0xFF2979FF.toInt(),
    ),
    AppInfo(
        name = "RuStore",
        description = "Официальный магазин приложений для Android",
        category = "Приложения",
        iconText = "R",
        iconColor = 0xFF00A86B.toInt(),
    ),
    AppInfo(
        name = "VK Образование",
        description = "Курсы и материалы для развития профессиональных навыков",
        category = "Образование",
        iconText = "V",
        iconColor = 0xFFFF9800.toInt(),
    ),
)
