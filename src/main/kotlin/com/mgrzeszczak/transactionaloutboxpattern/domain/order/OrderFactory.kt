package com.mgrzeszczak.transactionaloutboxpattern.domain.order

import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import java.util.*

class OrderFactory(private val time: Time) {

    fun create(command: CreateOrderCommand): Order {
        return Order(
                UUID.randomUUID().toString(),
                time.now(),
                command.amount
        )
    }

}