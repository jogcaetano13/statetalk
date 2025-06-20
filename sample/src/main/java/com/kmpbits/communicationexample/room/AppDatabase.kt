package com.kmpbits.communicationexample.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kmpbits.communicationexample.models.Challenge
import com.kmpbits.communicationexample.room.converters.DateConverter
import com.kmpbits.communicationexample.room.converters.ListStringConverter
import com.kmpbits.communicationexample.room.daos.ChallengeDao

@Database(
    entities = [
        Challenge::class
    ],
    version = 1
)
@TypeConverters(
    DateConverter::class,
    ListStringConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
}