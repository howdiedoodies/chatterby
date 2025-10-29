package com.howdiedoodies.chatterby.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val username: String,
    val lastOnline: Long? = null,
    val isOnline: Boolean = false,
    val thumbnailPath: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    val location: String? = null,
    val lastChecked: Long = 0
)
