package com.albuquerque.fakeserver.lib

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object FakeServer {

    private val responses = mutableMapOf<String, String>()
    const val HEADER_DELAY = "X-Fake-Server-Delay"
    const val HEADER_STATUS_CODE = "X-Fake-Server-Status-Code"

    fun initialize(context: Context) {
        val assetManager = context.assets
        val fileNames = assetManager.list("") ?: arrayOf()

        fileNames.forEach { fileName ->
            if (fileName.endsWith(".json")) {
                val inputStream = assetManager.open(fileName)
                val responseBody = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
                val endpoint = fileName.removeSuffix(".json").replace('_', '-')
                responses["/fake-server/$endpoint"] = responseBody
            }
        }
    }

    fun registerResponse(endpoint: String, response: String) {
        val formattedEndpoint = if (!endpoint.startsWith("/fake-server/")) {
            "/fake-server/$endpoint"
        } else {
            endpoint
        }
        responses[formattedEndpoint] = response
    }

    fun getResponse(url: String): String? {
        val matchResult = "https?://[^/]+(/.*)?(/fake-server/[^?]*)(\\?.*)?".toRegex().find(url)
        val path = matchResult?.groups?.get(2)?.value
        return responses[path]
    }
}