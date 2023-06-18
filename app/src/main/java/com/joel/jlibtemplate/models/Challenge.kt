package com.joel.jlibtemplate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joel.communication_android.models.PagingModel
import java.util.Date

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey
    val id: String,
    val name: String?,
    val slug: String?,
    val completedAt: Date?,
    val completedLanguages: List<String>?
) : PagingModel()