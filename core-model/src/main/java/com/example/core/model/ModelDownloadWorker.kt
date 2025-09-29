package com.example.core.model

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ModelDownloadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val id = inputData.getString(KEY_ID) ?: return@withContext Result.failure(msg("missing id"))
        val url = inputData.getString(KEY_URL) ?: return@withContext Result.failure(msg("missing url"))
        val sha = inputData.getString(KEY_SHA) ?: return@withContext Result.failure(msg("missing sha"))

        val registry = ModelRegistry(applicationContext)
        val outFile = registry.modelFile(id)

        try {
            if (outFile.exists()) {
                val existingSha = ModelRegistry.sha256(outFile)
                if (existingSha.equals(sha, ignoreCase = true)) {
                    return@withContext Result.success()
                } else {
                    outFile.delete()
                }
            }
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = 15000
            conn.readTimeout = 60000
            conn.instanceFollowRedirects = true
            conn.requestMethod = "GET"
            conn.connect()
            if (conn.responseCode !in 200..299) {
                return@withContext Result.failure(msg("http ${conn.responseCode}"))
            }
            val total = conn.contentLengthLong.takeIf { it > 0 }
            var readSum = 0L
            BufferedInputStream(conn.inputStream).use { input ->
                FileOutputStream(outFile).use { fos ->
                    val buf = ByteArray(1 shl 16)
                    while (true) {
                        val r = input.read(buf)
                        if (r <= 0) break
                        fos.write(buf, 0, r)
                        readSum += r
                        if (total != null && total > 0) {
                            setProgressAsync(Data.Builder().putInt(KEY_PROGRESS, ((readSum * 100) / total).toInt()).build())
                        }
                    }
                }
            }
            val finalSha = ModelRegistry.sha256(outFile)
            if (!finalSha.equals(sha, ignoreCase = true)) {
                outFile.delete()
                return@withContext Result.failure(msg("sha mismatch"))
            }
            Result.success()
        } catch (e: Exception) {
            outFile.delete()
            Result.retry()
        }
    }

    companion object {
        private const val KEY_ID = "id"
        private const val KEY_URL = "url"
        private const val KEY_SHA = "sha"
        const val KEY_PROGRESS = "progress"

        fun inputData(id: String, url: String, sha: String) = Data.Builder()
            .putString(KEY_ID, id)
            .putString(KEY_URL, url)
            .putString(KEY_SHA, sha)
            .build()

        private fun msg(m: String) = Data.Builder().putString("error", m).build()
    }
}