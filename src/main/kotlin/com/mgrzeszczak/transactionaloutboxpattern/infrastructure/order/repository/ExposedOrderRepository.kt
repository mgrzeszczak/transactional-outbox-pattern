package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order.repository

import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import com.mgrzeszczak.transactionaloutboxpattern.domain.order.Order
import com.mgrzeszczak.transactionaloutboxpattern.domain.order.OrderRepository
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.BaseRepository
import org.jetbrains.exposed.sql.insert

class ExposedOrderRepository(time: Time) : BaseRepository(time), OrderRepository {

    override fun save(order: Order) {
        OrderTable.insert {
            it[id] = order.id
            it[amount] = order.amount.value
            it[creationDate] = serializeZonedDateTime(order.creationDate)
        }
    }

}