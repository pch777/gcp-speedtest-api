package com.example.demo3

/* Følgende er kopiert fra Referanse-implementasjonen */

import com.example.demo3.SpeedtestEvent
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/speedtest")
class SpeedtestResource(val pubSub: PubSubTemplate,
                        @Value("speedtest") val speedtestTopic: String) {

    val gson: Gson = Gson()

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun postSpeedtest(@RequestBody event: SpeedtestEvent) : ResponseEntity<SpeedtestEvent> {
        return try {
            this.pubSub.publish(speedtestTopic, gson.toJson(event)).get()
            ResponseEntity.ok(event);
        } catch (t: Throwable) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    }
}