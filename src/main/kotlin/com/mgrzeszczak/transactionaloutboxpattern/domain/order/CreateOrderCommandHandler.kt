package com.mgrzeszczak.transactionaloutboxpattern.domain.order

import com.mgrzeszczak.transactionaloutboxpattern.domain.CommandHandler
import com.mgrzeszczak.transactionaloutboxpattern.domain.EventPublisher
import com.mgrzeszczak.transactionaloutboxpattern.domain.order.event.OrderEventFactory
import javax.transaction.Transactional

open class CreateOrderCommandHandler(private val orderRepository: OrderRepository,
                                     private val orderFactory: OrderFactory,
                                     private val eventPublisher: EventPublisher,
                                     private val orderEventFactory: OrderEventFactory) : CommandHandler<CreateOrderCommand> {

    @Transactional
    override fun handle(command: CreateOrderCommand) {
        val order = orderFactory.create(command)
        orderRepository.save(order)
        eventPublisher.publish(orderEventFactory.orderCreated(order))
    }

    override fun supports() = CreateOrderCommand::class

}