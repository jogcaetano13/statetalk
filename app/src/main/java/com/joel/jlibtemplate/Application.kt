package com.joel.jlibtemplate

import android.app.Application
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
                clientModule { baseUrl = BASE_URL },
                databaseModule(),
                repositoriesModule(),
                viewModelModule()
            )
        }
    }
}