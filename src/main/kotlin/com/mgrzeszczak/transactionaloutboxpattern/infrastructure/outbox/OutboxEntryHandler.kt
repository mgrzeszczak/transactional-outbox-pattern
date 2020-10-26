package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

interface OutboxEntryHandler {

    fun handle(entry: OutboxEntry)

    fun supports(): OutboxEntryType

}