package com.albuquerque.fakeserver.service

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET

interface MyApiService {

    @GET("fake-server/lorem-ipsum")
    fun getFakeLoremIpsum(): Single<ResponseBody>

    @GET("e2ef4a32-3d7f-405f-af3b-76dbc100b030")
    fun getLoremIpsum(): Single<ResponseBody>
}