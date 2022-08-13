package com.joel.jlibtemplate.room.daos

import androidx.paging.PagingSource
import androidx.room.*
import com.joel.jlibtemplate.models.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    @Query("SELECT * FROM challenges")
    fun getChallenges(): PagingSource<Int, Challenge>

    @Query("SELECT * FROM challenges LIMIT 1")
    suspend fun getChallenge(): Challenge?

    @Query("SELECT * FROM challenges")
    fun getChallengesFlow(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges")
    suspend fun getChallengesSuspended(): List<Challenge>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(challenges: List<Challenge>): List<Long>

    @Transaction
    suspend fun replace(challenges: List<Challenge>) {
        insert(challenges)
    }

    @Query("DELETE FROM challenges")
    suspend fun deleteAll(): Int
}