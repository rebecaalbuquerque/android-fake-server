package com.albuquerque.fakeserver.lib

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object FakeServer {
    private val responses = mutableMapOf<String, String>()

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

    fun getResponse(url: String): String? {
        val regex = "https?://[^/]+(/.*)?(/fake-server/[^?]*)(\\?.*)?".toRegex()
        val matchResult = regex.find(url)

        val path = matchResult?.groups?.get(2)?.value

        return responses[path]
    }
}