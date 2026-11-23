package com.example.tosh.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tosh.domain.App
import com.example.tosh.domain.GetAppsUseCase
import com.example.tosh.domain.LoadRatingsInParallelUseCase
import com.example.tosh.domain.LoadRatingsSequentiallyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val getApps: GetAppsUseCase,
    private val loadRatingsSequentially: LoadRatingsSequentiallyUseCase,
    private val loadRatingsInParallel: LoadRatingsInParallelUseCase,
) : ViewModel() {
    private val _apps = MutableStateFlow<List<App>>(emptyList())
    val apps: StateFlow<List<App>> = _apps.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackbarMessages: SharedFlow<String> = _snackbarMessages.asSharedFlow()

    private var ratingsJob: Job? = null

    init {
        loadApps()
    }

    fun onToolbarClick(message: String) {
        _snackbarMessages.tryEmit(message)
    }

    fun onLoadRatingsSequentially() {
        loadRatings(sequential = true)
    }

    fun onLoadRatingsInParallel() {
        loadRatings(sequential = false)
    }

    private fun loadRatings(sequential: Boolean) {
        val ids = _apps.value.map { it.id }
        if (ids.isEmpty()) return
        ratingsJob?.cancel()
        ratingsJob = viewModelScope.launch {
            val time = measureTimeMillis {
                val ratings = if (sequential) {
                    loadRatingsSequentially(ids)
                } else {
                    loadRatingsInParallel(ids)
                }
                _apps.update { apps ->
                    apps.map { app -> app.copy(rating = ratings[app.id]) }
                }
            }
            val message = if (sequential) {
                "По очереди: $time мс, приложений: ${ids.size}"
            } else {
                "Параллельно: $time мс, приложений: ${ids.size}"
            }
            Log.d(TAG, message)
            _snackbarMessages.tryEmit(message)
        }
    }

    private fun loadApps() {
        viewModelScope.launch {
            Log.d(TAG, "loadApps")
            runCatching { getApps() }
                .onSuccess { apps -> _apps.value = apps }
                .onFailure { error -> Log.e(TAG, "loadApps", error) }
        }
    }

    private companion object {
        const val TAG = "AppsViewModel"
    }
}
