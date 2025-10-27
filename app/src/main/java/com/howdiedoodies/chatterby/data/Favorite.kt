package com.howdiedoodies.chatterby.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val username: String,
    val lastOnline: Date? = null,
    val isOnline: Boolean = false,
    val thumbnailPath: String? = null,
    val currentGoal: Int? = null,
    val targetGoal: Int? = null
)