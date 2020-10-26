package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import com.mgrzeszczak.transactionaloutboxpattern.domain.Event
import com.mgrzeszczak.transactionaloutboxpattern.domain.EventPublisher

class OutboxEventPublisher(private val outboxRepository: OutboxRepository,
                           private val outboxEntryFactory: OutboxEntryFactory) : EventPublisher {

    override fun publish(event: Event) {
        val outboxEntry = outboxEntryFactory.fromEvent(event)
        outboxRepository.insert(outboxEntry)
    }

}