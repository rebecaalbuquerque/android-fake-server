package com.albuquerque.fakeserver.service

import com.albuquerque.fakeserver.lib.FakeServer
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header

interface MyApiService {

    @GET("fake-server/lorem-ipsum")
    fun getFakeLoremIpsum(
        @Header(FakeServer.HEADER_DELAY) delay: Int? = null,
        @Header(FakeServer.HEADER_STATUS_CODE) statusCode: Int? = null
    ): Single<ResponseBody>

    @GET("e2ef4a32-3d7f-405f-af3b-76dbc100b030")
    fun getLoremIpsum(): Single<ResponseBody>
}