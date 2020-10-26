package com.mgrzeszczak.transactionaloutboxpattern.domain.order.event

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mgrzeszczak.transactionaloutboxpattern.domain.Event
import com.mgrzeszczak.transactionaloutboxpattern.domain.Topic
import com.mgrzeszczak.transactionaloutboxpattern.domain.valueobject.Money
import java.time.ZonedDateTime

@Topic("orders")
data class OrderCreatedEvent(val id: String,
                             val creationDate: ZonedDateTime,
                             val amount: Money) : Event {
    @JsonIgnore
    override fun key() = id

    @JsonIgnore
    override fun version(): Int = 1
}