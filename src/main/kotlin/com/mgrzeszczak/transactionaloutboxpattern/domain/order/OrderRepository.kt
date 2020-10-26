package com.mgrzeszczak.transactionaloutboxpattern.domain.order

interface OrderRepository {

    fun save(order: Order)

}