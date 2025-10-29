package com.howdiedoodies.chatterby.scraper

import com.howdiedoodies.chatterby.scraper.model.Cam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class ChaturbateScraper {

    suspend fun search(query: String = "", gender: String = "", nrPages: Int = 1): List<Cam> = withContext(Dispatchers.IO) {
        val cams = mutableListOf<Cam>()
        for (i in 1..nrPages) {
            val url = "https://chaturbate.com/?" +
                "keywords=$query" +
                "&gender=$gender" +
                "&page=$i"
            val doc = Jsoup.connect(url).get()
            val elements = doc.select(".room_list_room")
            for (element in elements) {
                val username = element.select(".title a").text()
                val age = element.select(".age").text().toIntOrNull()
                val location = element.select(".location").text()
                val uptimeMin = element.select(".time").text().toIntOrNull()
                val spectators = element.select(".cams").text().toIntOrNull()
                cams.add(Cam(username, age, location, uptimeMin, spectators))
            }
        }
        cams
    }
}
