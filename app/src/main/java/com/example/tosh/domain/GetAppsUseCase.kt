package com.example.tosh.domain

import javax.inject.Inject

class GetAppsUseCase @Inject constructor(
    private val repository: AppsRepository,
) {
    suspend operator fun invoke(): List<App> = repository.getApps()
}
