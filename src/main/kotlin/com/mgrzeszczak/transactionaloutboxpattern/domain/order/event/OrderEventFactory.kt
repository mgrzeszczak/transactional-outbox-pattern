package com.mgrzeszczak.transactionaloutboxpattern.domain.order.event

import com.mgrzeszczak.transactionaloutboxpattern.domain.order.Order

class OrderEventFactory {

    fun orderCreated(order: Order) = OrderCreatedEvent(order.id, order.creationDate, order.amount)

}