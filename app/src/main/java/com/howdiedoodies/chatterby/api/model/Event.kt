package com.howdiedoodies.chatterby.api.model

data class Event(
    val method: String,
    val broadcaster: Broadcaster?,
    val media: Media?,
    val message: Message?,
    val subject: Subject?,
    val tip: Tip?,
    val user: User?
)
