package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import java.time.ZonedDateTime

interface OutboxRepository {

    fun insert(entry: OutboxEntry)
    fun update(entry: OutboxEntry)
    fun findNext(now: ZonedDateTime, batchSize: Int, maxRetries: Int): List<OutboxEntry>
    fun rescue(olderThan: ZonedDateTime)

}