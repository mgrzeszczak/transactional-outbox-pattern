package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order

import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import com.mgrzeszczak.transactionaloutboxpattern.domain.EventPublisher
import com.mgrzeszczak.transactionaloutboxpattern.domain.order.CreateOrderCommandHandler
import com.mgrzeszczak.transactionaloutboxpattern.domain.order.OrderFactory
import com.mgrzeszczak.transactionaloutboxpattern.domain.order.OrderRepository
import com.mgrzeszczak.transactionaloutboxpattern.domain.order.event.OrderEventFactory
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order.repository.ExposedOrderRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderConfig {

    @Bean
    fun orderFactory(time: Time) = OrderFactory(time)

    @Bean
    fun createOrderCommandHandler(orderRepository: OrderRepository,
                                  orderFactory: OrderFactory,
                                  eventPublisher: EventPublisher,
                                  orderEventFactory: OrderEventFactory) =
            CreateOrderCommandHandler(orderRepository, orderFactory, eventPublisher, orderEventFactory)

    @Bean
    fun orderRepository(time: Time) = ExposedOrderRepository(time)

    @Bean
    fun orderEventFactory() = OrderEventFactory()

}