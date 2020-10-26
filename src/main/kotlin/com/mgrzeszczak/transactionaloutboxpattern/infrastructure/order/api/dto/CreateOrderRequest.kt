package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order.api.dto

import com.mgrzeszczak.transactionaloutboxpattern.domain.order.CreateOrderCommand
import com.mgrzeszczak.transactionaloutboxpattern.domain.valueobject.Money

data class CreateOrderRequest constructor(
        val amount: Money
) {
    fun toCommand() = CreateOrderCommand(amount)
}