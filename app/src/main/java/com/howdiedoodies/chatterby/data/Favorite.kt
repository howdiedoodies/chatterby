package com.howdiedoodies.chatterby.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey
    val username: String,
    val gender: String? = null,
    val age: Int? = null,
    val subject: String? = null,
    val location: String? = null,
    val isOnline: Boolean = false,
    val lastOnline: Long? = null,
    val thumbnailPath: String? = null
)
