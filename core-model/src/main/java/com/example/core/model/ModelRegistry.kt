package com.example.core.model

import android.content.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.security.MessageDigest

@Serializable
data class ModelDescriptor(
    val id: String,
    val displayName: String,
    val url: String,
    val sha256: String,
    val bytes: Long = 0
)

@Serializable
private data class ModelSources(@SerialName("models") val models: List<ModelDescriptor>)

class ModelRegistry(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadRemoteList(): List<ModelDescriptor> {
        val assetStream = context.assets.open("model_sources.json")
        assetStream.use { input ->
            val text = input.readBytes().decodeToString()
            return json.decodeFromString(ModelSources.serializer(), text).models
        }
    }

    fun modelFile(id: String): File = File(context.filesDir, idToFilename(id))

    private fun idToFilename(id: String) = when {
        id.endsWith(".gguf") -> id
        else -> "ggml-$id.gguf"
    }

    companion object {
        fun sha256(file: File): String {
            val digest = MessageDigest.getInstance("SHA-256")
            file.inputStream().use { fis ->
                val buf = ByteArray(8192)
                while (true) {
                    val r = fis.read(buf)
                    if (r <= 0) break
                    digest.update(buf, 0, r)
                }
            }
            return digest.digest().joinToString("") { b -> "%02x".format(b) }
        }
    }
}