package com.example.contextawaretranslator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.core.model.ModelDescriptor

class MainActivity : ComponentActivity() {
	private val modelVm: ModelViewModel by viewModels()

	private val audioPerm = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			audioPerm.launch(Manifest.permission.RECORD_AUDIO)
		}
		modelVm.loadList()
		setContent { MaterialTheme { ModelScreen(modelVm) } }
	}
}

@Composable
private fun ModelScreen(vm: ModelViewModel) {
	val models by vm.models.collectAsState()
	val selected by vm.selected.collectAsState()
	val ready by vm.modelReady.collectAsState()
	var loadResult by remember { mutableStateOf<String?>(null) }

	Surface(Modifier.fillMaxSize()) {
		Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
			Text("Models", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
			if (models.isEmpty()) {
				CircularProgressIndicator()
			} else {
				LazyColumn(Modifier.weight(1f)) {
					items(models) { m ->
						ModelRow(m, selected?.id == m.id, onDownload = { vm.ensureDownloaded(m) })
					}
				}
			}
			Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
				Text(selected?.displayName ?: "None selected")
				Button(onClick = {
					val path = vm.filePathForSelected()
					if (path == null) {
						loadResult = "No model file yet"
					} else {
						// TODO integrate actual whisper load call
						loadResult = "Pretend loaded: $path"
					}
				}, enabled = ready) { Text("Load Model") }
			}
			loadResult?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
		}
	}
}

@Composable
private fun ModelRow(desc: ModelDescriptor, selected: Boolean, onDownload: () -> Unit) {
	ElevatedCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
		Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
			Column(Modifier.weight(1f)) {
				Text(desc.displayName, fontWeight = FontWeight.Medium)
				Text(desc.id, style = MaterialTheme.typography.bodySmall)
			}
			Button(onClick = onDownload) { Text(if (selected) "Recheck" else "Download") }
		}
	}
}
