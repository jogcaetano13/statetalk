package com.joel.jlibtemplate

import android.app.Application
import com.joel.communication_core.alias.Header
import com.joel.communication_core.enums.HttpHeader
import com.joel.communication_core.enums.LogLevel
import com.joel.jlibtemplate.di.clientModule
import com.joel.jlibtemplate.di.databaseModule
import com.joel.jlibtemplate.di.repositoriesModule
import com.joel.jlibtemplate.di.viewModelModule
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

                    cache {
                        file = this@Application.externalCacheDir
                    }
                },
                databaseModule(),
                repositoriesModule(),
                viewModelModule()
            )
        }
    }
}