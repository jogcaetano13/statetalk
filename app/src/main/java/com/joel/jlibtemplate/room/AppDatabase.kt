package com.joel.jlibtemplate.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.room.converters.DateConverter
import com.joel.jlibtemplate.room.converters.ListStringConverter
import com.joel.jlibtemplate.room.daos.ChallengeDao

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