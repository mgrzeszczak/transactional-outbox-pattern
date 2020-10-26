package com.mgrzeszczak.transactionaloutboxpattern.infrastructure

import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import java.time.Instant
import java.time.ZonedDateTime

open class BaseRepository(protected val time: Time) {

    fun serializeZonedDateTime(date: ZonedDateTime) = date.toInstant().toEpochMilli()
    fun deserializeZonedDateTime(epochMillis: Long): ZonedDateTime? {
        return when (epochMillis) {
            0L -> null
            else -> Instant.ofEpochMilli(epochMillis).atZone(time.zoneId())
        }
    }

}