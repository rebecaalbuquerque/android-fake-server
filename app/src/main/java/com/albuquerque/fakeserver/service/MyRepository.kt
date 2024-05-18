package com.albuquerque.fakeserver.service

import io.reactivex.Single
import okhttp3.ResponseBody

class MyRepository(
    private val apiService: MyApiService
) {

    fun fetchFakeLoremIpsum(): Single<ResponseBody> {
        return apiService.getFakeLoremIpsum()
    }

    fun fetchLoremIpsum(): Single<ResponseBody> {
        return apiService.getLoremIpsum()
    }
}