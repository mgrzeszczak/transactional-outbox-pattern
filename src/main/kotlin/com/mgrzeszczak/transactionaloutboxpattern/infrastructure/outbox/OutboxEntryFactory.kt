package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import com.mgrzeszczak.transactionaloutboxpattern.domain.Event
import com.mgrzeszczak.transactionaloutboxpattern.domain.Topic
import java.util.*
import kotlin.reflect.full.findAnnotation

class OutboxEntryFactory(private val time: Time, private val objectMapper: ObjectMapper) {

    fun fromEvent(event: Event): OutboxEntry {
        val topic = event::class.findAnnotation<Topic>()!!
        val now = time.now()
        return OutboxEntry(
                id = UUID.randomUUID().toString(),
                creationDate = now,
                nextExecDate = now,
                completionDate = null,
                status = OutboxEntryStatus.OPEN,
                type = OutboxEntryType.EVENT,
                data = objectMapper.writeValueAsString(EventData(event, topic.value, event.key(), event.version())),
                retries = 0
        )
    }

}

data class EventData(val event: Event, val topic: String, val partitionKey: String, val version: Int)