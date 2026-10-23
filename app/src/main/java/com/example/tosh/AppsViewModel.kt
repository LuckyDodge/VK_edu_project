package com.example.tosh

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AppsViewModel : ViewModel() {
    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackbarMessages: SharedFlow<String> = _snackbarMessages.asSharedFlow()

    init {
        loadApps()
    }

    fun onToolbarClick(message: String) {
        _snackbarMessages.tryEmit(message)
    }

    private fun loadApps() {
        Log.d(TAG, "loadApps")
        _apps.value = sampleApps
    }

    private companion object {
        const val TAG = "AppsViewModel"
    }
}
