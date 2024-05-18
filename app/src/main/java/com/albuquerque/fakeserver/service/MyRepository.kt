package com.albuquerque.fakeserver.service

import io.reactivex.Single
import okhttp3.ResponseBody

class MyRepository(
    private val apiService: MyApiService
) {

    fun fetchFakeLoremIpsum(
        delay: Int? = null,
        statusCode: String? = null
    ): Single<ResponseBody> {
        return apiService.getFakeLoremIpsum(delay, statusCode)
    }

    fun fetchLoremIpsum(): Single<ResponseBody> {
        return apiService.getLoremIpsum()
    }
}