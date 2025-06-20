package com.kmpbits.communicationexample

import android.app.Application
import com.kmpbits.communicationexample.di.clientModule
import com.kmpbits.communicationexample.di.databaseModule
import com.kmpbits.communicationexample.di.repositoriesModule
import com.kmpbits.communicationexample.di.viewModelModule
import com.kmpbits.communication_core.alias.Header
import com.kmpbits.communication_core.enums.HttpHeader
import com.kmpbits.communication_core.enums.LogLevel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(
                clientModule {
                    baseUrl = BASE_URL
                    logLevel = LogLevel.Body

                    header(Header(HttpHeader.custom("custom-header"), "This is a custom header"))

//                    cache {
//                        file = this@Application.externalCacheDir
//                    }
                },
                databaseModule(),
                repositoriesModule(),
                viewModelModule()
            )
        }
    }
}