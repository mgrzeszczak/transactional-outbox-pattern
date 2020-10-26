package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox.handler.EventOutboxEntryHandler
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate

@Configuration
@EnableConfigurationProperties(OutboxProperties::class)
class OutboxConfig {

    @Bean
    fun outboxRepository(time: Time) =
            ExposedOutboxRepository(time)

    @Bean
    fun outboxEventPublisher(outboxRepository: OutboxRepository,
                             outboxEntryFactory: OutboxEntryFactory) =
            OutboxEventPublisher(outboxRepository, outboxEntryFactory)

    @Bean
    fun outboxEntryFactory(time: Time, objectMapper: ObjectMapper) =
            OutboxEntryFactory(time, objectMapper)

    @Bean
    fun outboxEntryWorker(handlers: List<OutboxEntryHandler>,
                          time: Time,
                          outboxProperties: OutboxProperties) =
            OutboxEntryWorker(handlers, time, outboxProperties)

    @Bean
    fun outboxScheduler(exposedOutboxRepository: ExposedOutboxRepository,
                        outboxProperties: OutboxProperties,
                        outboxEntryWorker: OutboxEntryWorker,
                        time: Time) =
            OutboxScheduler(exposedOutboxRepository, outboxProperties, outboxEntryWorker, time)

    @Bean
    fun eventOutboxEntryHandler(objectMapper: ObjectMapper,
                                kafkaTemplate: KafkaTemplate<String, Any>,
                                time: Time) =
            EventOutboxEntryHandler(kafkaTemplate, objectMapper, time)

}