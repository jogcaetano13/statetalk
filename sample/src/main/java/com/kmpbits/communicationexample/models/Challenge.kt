package com.kmpbits.communicationexample.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey
    val id: String,
    val name: String?,
    val slug: String?,
    val completedAt: Date?,
    val completedLanguages: List<String>?
) : com.kmpbits.communication_paging.models.PagingModel()