package com.mgrzeszczak.transactionaloutboxpattern.domain.order

import com.mgrzeszczak.transactionaloutboxpattern.domain.valueobject.Money
import java.time.ZonedDateTime

data class Order(
        val id: String,
        val creationDate: ZonedDateTime,
        val amount: Money
)