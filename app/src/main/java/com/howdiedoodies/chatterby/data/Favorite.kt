// app/src/main/java/com/howdiedoodies/chatterby/data/Favorite.kt  (or kotlin/)
package com.howdiedoodies.chatterby.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val username: String,
    val lastOnline: Long? = null   // ← LONG, NOT DATE
)