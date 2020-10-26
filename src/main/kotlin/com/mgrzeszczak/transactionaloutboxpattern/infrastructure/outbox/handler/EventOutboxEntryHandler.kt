package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox.OutboxEntry
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox.OutboxEntryHandler
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox.OutboxEntryType
import org.springframework.kafka.core.KafkaTemplate
import java.time.ZonedDateTime
import java.util.*

class EventOutboxEntryHandler(private val kafkaTemplate: KafkaTemplate<String, Any>,
                              private val objectMapper: ObjectMapper,
                              private val time: Time) : OutboxEntryHandler {

    override fun handle(entry: OutboxEntry) {
        if (entry.type != OutboxEntryType.EVENT) {
            throw IllegalStateException("Type ${entry.type} is not supported")
        }
        val eventData = objectMapper.readValue<AnyEventData>(entry.data)
        val kafkaEvent = KafkaEvent(UUID.randomUUID().toString(), time.now(), eventData.version, eventData.event)
        kafkaTemplate.send(eventData.topic, eventData.partitionKey, kafkaEvent).get()
    }

    override fun supports() = OutboxEntryType.EVENT
}

data class KafkaEvent(val id: String, val timestamp: ZonedDateTime, val version: Int, val data: Any)
data class AnyEventData(val topic: String, val partitionKey: String, val version: Int, val event: Any)