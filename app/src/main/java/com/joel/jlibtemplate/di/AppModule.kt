package com.joel.jlibtemplate.di

import android.content.Context
import androidx.room.Room
import com.joel.jlibtemplate.MainViewModel
import com.joel.jlibtemplate.respositories.ChallengeRepository
import com.joel.jlibtemplate.respositories.ChallengeRepositoryImpl
import com.joel.jlibtemplate.room.AppDatabase
import com.joel.jlibtemplate.room.daos.ChallengeDao
import com.joel.statetalk_core.client.Client
import com.joel.statetalk_core.client.ClientBuilder
import com.joel.statetalk_core.extensions.stateTalkClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun clientModule(builder: ClientBuilder. () -> Unit) = module {
    single { stateTalkClient(builder) }
}

fun repositoriesModule() = module {
    fun provideChallengeRepository(
        client: Client,
        dao: ChallengeDao
    ): ChallengeRepository = ChallengeRepositoryImpl(client, dao)

    single { provideChallengeRepository(get(), get()) }
}

fun databaseModule() = module {
    fun provideDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
        .fallbackToDestructiveMigration()
        .build()

    fun provideMoviesDao(database: AppDatabase) = database.challengeDao()

    single { provideDatabase(androidContext()) }
    single { provideMoviesDao(get()) }
}

fun viewModelModule() = module {
    viewModel { MainViewModel(get()) }
}