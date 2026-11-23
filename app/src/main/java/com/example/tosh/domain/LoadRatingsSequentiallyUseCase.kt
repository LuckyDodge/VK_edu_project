package com.example.tosh.domain

import javax.inject.Inject

class LoadRatingsSequentiallyUseCase @Inject constructor(
    private val repository: AppsRepository,
) {
    suspend operator fun invoke(ids: List<String>): Map<String, Float> =
        repository.loadRatingsSequentially(ids)
}
