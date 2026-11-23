package com.example.tosh.presentation

sealed interface InstallState {
    data object NotInstalled : InstallState
    data class Installing(val progress: Int) : InstallState
    data object Installed : InstallState
}

sealed interface AppDetailsState {
    data class Content(
        val installState: InstallState,
    ) : AppDetailsState
}
