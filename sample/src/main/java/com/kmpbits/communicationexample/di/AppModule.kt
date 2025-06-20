package com.kmpbits.communicationexample.di

import android.content.Context
import androidx.room.Room
import com.kmpbits.communication_core.client.Client
import com.kmpbits.communication_core.client.ClientBuilder
import com.kmpbits.communication_core.extensions.communicationClient
import com.kmpbits.communicationexample.MainViewModel
import com.kmpbits.communicationexample.respositories.ChallengeRepository
import com.kmpbits.communicationexample.respositories.ChallengeRepositoryImpl
import com.kmpbits.communicationexample.room.AppDatabase
import com.kmpbits.communicationexample.room.daos.ChallengeDao
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun clientModule(builder: ClientBuilder. () -> Unit) = module {
    single { communicationClient(builder) }
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