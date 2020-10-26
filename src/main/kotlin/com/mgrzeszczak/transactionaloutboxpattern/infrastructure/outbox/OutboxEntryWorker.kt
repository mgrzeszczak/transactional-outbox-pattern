package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import mu.KotlinLogging
import java.math.BigDecimal
import java.time.Duration
import java.time.ZonedDateTime

class OutboxEntryWorker(handlers: List<OutboxEntryHandler>,
                        private val time: Time,
                        private val outboxProperties: OutboxProperties) {

    private val logger = KotlinLogging.logger { }
    private val durationUnit = Duration.ofMinutes(1L)
    private val handlerMap = handlers.associateBy { it.supports() }

    fun execute(entry: OutboxEntry): OutboxEntry {
        try {
            handlerMap[entry.type]!!.handle(entry)
            return entry.copy(
                    completionDate = time.now(),
                    status = OutboxEntryStatus.DONE
            )
        } catch (e: Exception) {
            logger.warn(e) { "Failed to execute entry: $entry" }
            val newRetries = entry.retries + 1
            return entry.copy(
                    retries = newRetries,
                    nextExecDate = nextExecDate(newRetries.toLong(), time.now()),
                    completionDate = if (newRetries == outboxProperties.maxRetries) time.now() else null,
                    status = if (newRetries == outboxProperties.maxRetries) OutboxEntryStatus.ERROR else OutboxEntryStatus.OPEN
            )
        }
    }

    private fun nextExecDate(retries: Long, now: ZonedDateTime): ZonedDateTime {
        return now.plus(durationUnit.multipliedBy(BigDecimal.valueOf(retries + 1).pow(2).toLong()))
    }

}