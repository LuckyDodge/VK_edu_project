package com.example.tosh.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AppDetailsViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<AppDetailsState>(
        AppDetailsState.Content(InstallState.NotInstalled),
    )
    val state: StateFlow<AppDetailsState> = _state.asStateFlow()

    private var installJob: Job? = null

    fun onInstallClick() {
        val current = _state.value
        if (current !is AppDetailsState.Content) return

        if (current.installState is InstallState.Installing) {
            installJob?.cancel()
        } else {
            installJob = viewModelScope.launch {
                try {
                    emulateInstall()
                } catch (e: CancellationException) {
                    _state.value = AppDetailsState.Content(InstallState.NotInstalled)
                    throw e
                }
            }
        }
    }

    private suspend fun emulateInstall() {
        for (progress in 0..100 step 5) {
            delay(150)
            coroutineContext.ensureActive()
            _state.update { state ->
                val content = state as? AppDetailsState.Content ?: return@update state
                content.copy(installState = InstallState.Installing(progress))
            }
        }
        _state.update { state ->
            val content = state as? AppDetailsState.Content ?: return@update state
            content.copy(installState = InstallState.Installed)
        }
    }
}
