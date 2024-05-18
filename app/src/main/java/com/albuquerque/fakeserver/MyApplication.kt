package com.albuquerque.fakeserver

import android.app.Application
import com.albuquerque.fakeserver.di.networkModule
import com.albuquerque.fakeserver.lib.FakeServer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        FakeServer.initialize(this)

        startKoin {
            androidContext(this@MyApplication)
            modules(networkModule)
        }
    }
}