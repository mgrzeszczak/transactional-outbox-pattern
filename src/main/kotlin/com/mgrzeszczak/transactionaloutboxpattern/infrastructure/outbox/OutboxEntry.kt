package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import java.time.ZonedDateTime

data class OutboxEntry(
        val id: String,
        val creationDate: ZonedDateTime,
        val nextExecDate: ZonedDateTime,
        val completionDate: ZonedDateTime?,
        val status: OutboxEntryStatus,
        val type: OutboxEntryType,
        val data: String,
        val retries: Int
)

enum class OutboxEntryStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
    ERROR
}

enum class OutboxEntryType {
    EVENT
}