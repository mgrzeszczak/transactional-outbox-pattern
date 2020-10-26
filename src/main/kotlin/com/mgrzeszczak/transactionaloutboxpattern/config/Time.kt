package com.mgrzeszczak.transactionaloutboxpattern.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.Instant
import java.time.ZonedDateTime

class Time(private val clock: Clock) {

    fun now() = ZonedDateTime.now(clock)

    fun toZonedDateTime(epochSecond: Long) = Instant.ofEpochSecond(epochSecond).atZone(clock.zone)

    fun zoneId() = clock.zone

}

@Configuration
class TimeConfig {

    @Bean
    fun time() = Time(Clock.systemDefaultZone())

}