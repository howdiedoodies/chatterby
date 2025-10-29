package com.howdiedoodies.chatterby.api.model

data class Broadcaster(val username: String)
data class Media(val id: String)
data class Message(val user: String, val message: String)
data class Subject(val subject: String)
data class Tip(val user: String, val amount: Int)
data class User(val username: String)
