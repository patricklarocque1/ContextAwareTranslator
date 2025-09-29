package com.example.contextawaretranslator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.core.model.ModelDescriptor
import com.example.core.model.ModelDownloadWorker
import com.example.core.model.ModelRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ModelViewModel(app: Application) : AndroidViewModel(app) {
    private val registry by lazy { ModelRegistry(app) }
    private val wm by lazy { WorkManager.getInstance(app) }

    private val _models = MutableStateFlow<List<ModelDescriptor>>(emptyList())
    val models: StateFlow<List<ModelDescriptor>> = _models

    private val _selected = MutableStateFlow<ModelDescriptor?>(null)
    val selected: StateFlow<ModelDescriptor?> = _selected

    private val _downloading = MutableStateFlow<Map<String, Int>>(emptyMap())
    val downloading: StateFlow<Map<String, Int>> = _downloading

    private val _ready = MutableStateFlow(false)
    val modelReady: StateFlow<Boolean> = _ready

    fun loadList() {
        viewModelScope.launch {
            runCatching { registry.loadRemoteList() }
                .onSuccess { _models.value = it }
        }
    }

    fun ensureDownloaded(desc: ModelDescriptor) {
        val file = registry.modelFile(desc.id)
        if (file.exists()) {
            _selected.value = desc
            _ready.value = true
            return
        }
        val req = OneTimeWorkRequestBuilder<ModelDownloadWorker>()
            .setInputData(ModelDownloadWorker.inputData(desc.id, desc.url, desc.sha256))
            .build()
        val uniqueName = "download-${desc.id}"
        wm.enqueueUniqueWork(uniqueName, ExistingWorkPolicy.KEEP, req)
        observe(uniqueName, desc.id)
    }

    private fun observe(name: String, id: String) {
        // WorkManager LiveData interop replaced by polling via coroutine if needed; simplified here
        viewModelScope.launch {
            // no direct polling implementation due to environment; placeholder
        }
    }

    fun filePathForSelected(): String? = _selected.value?.let { registry.modelFile(it.id).absolutePath }
}