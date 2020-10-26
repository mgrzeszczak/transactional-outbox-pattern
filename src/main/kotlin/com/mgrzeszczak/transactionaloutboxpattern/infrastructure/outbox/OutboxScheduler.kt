package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.scheduling.annotation.Scheduled
import java.time.Duration

open class OutboxScheduler(private val outboxRepository: OutboxRepository,
                           private val outboxProperties: OutboxProperties,
                           private val outboxEntryWorker: OutboxEntryWorker,
                           private val time: Time) {

    @Scheduled(cron = "\${outbox.rescue-cron}")
    open fun rescue() {
        outboxRepository.rescue(time.now().minus(outboxProperties.rescueOlderThan))
    }

    @Scheduled(cron = "\${outbox.cron}")
    open fun execute() {
        val entries = outboxRepository.findNext(
                time.now(),
                outboxProperties.batchSize,
                outboxProperties.maxRetries
        )
        entries.map {
            outboxEntryWorker.execute(it)
        }.forEach(outboxRepository::update)
    }

}

@ConstructorBinding
@ConfigurationProperties("outbox")
data class OutboxProperties(
        val batchSize: Int = 50,
        val maxRetries: Int = 3,
        val rescueOlderThan: Duration = Duration.ofMinutes(15)
)