package com.albuquerque.fakeserver.lib

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.MediaType
import okhttp3.ResponseBody

class FakeServerInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val url = request.url().toString()

        return if (url.contains("/fake-server")) {
            val data = FakeServer.getResponse(url)
            val response = Response.Builder()
                .request(request)
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .addHeader("content-type", "application/json")

            if (data != null) {
                response.code(200)
                    .message("OK")
                    .body(ResponseBody.create(MediaType.get("application/json"), data))
                    .build()
            } else {
                response.code(404)
                    .message("Not Found")
                    .body(ResponseBody.create(MediaType.get("application/json"), "{}"))
                    .build()
            }

        } else {
            chain.proceed(request)
        }
    }
}