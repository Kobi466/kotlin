package com.example.bluromatic.ui.theme

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import com.example.bluromatic.BluromaticApplication
import com.example.bluromatic.R
import com.example.bluromatic.WorkerKeys
import com.example.bluromatic.data.BluromaticRepository
import kotlinx.coroutines.flow.*

sealed interface BlurUiState {
    object Default : BlurUiState
    object Loading : BlurUiState
    data class Complete(val outputUri: Uri) : BlurUiState
    object Error : BlurUiState
}

class BlurViewModel(
    application: Application,
    private val bluromaticRepository: BluromaticRepository
) : AndroidViewModel(application) {

    var imageUri: String =
        Uri.parse(
            "android.resource://${application.packageName}/${R.drawable.android_cupcake}"
        ).toString()

    val blurUiState: StateFlow<BlurUiState> =
        bluromaticRepository.outputWorkInfo
            .map { info ->
                when {
                    info == null -> BlurUiState.Default
                    info.state == WorkInfo.State.SUCCEEDED -> {
                        val uri = info.outputData.getString(WorkerKeys.IMAGE_URI)
                        if (uri != null) BlurUiState.Complete(Uri.parse(uri))
                        else BlurUiState.Error
                    }
                    info.state == WorkInfo.State.FAILED ||
                            info.state == WorkInfo.State.CANCELLED -> BlurUiState.Error
                    info.state.isFinished -> BlurUiState.Default
                    else -> BlurUiState.Loading
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BlurUiState.Default
            )

    fun applyBlur(blurLevel: Int) {
        bluromaticRepository.applyBlur(imageUri, blurLevel)
    }

    fun cancelWork() {
        bluromaticRepository.cancelWork()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as BluromaticApplication
                BlurViewModel(app, app.container.bluromaticRepository)
            }
        }
    }
}