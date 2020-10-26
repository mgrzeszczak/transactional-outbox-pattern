package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order.api

import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.command.CommandExecutor
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order.api.dto.CreateOrderRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/order")
class OrderController(private val commandExecutor: CommandExecutor) {

    @PostMapping
    fun create(@RequestBody request: CreateOrderRequest) {
        commandExecutor.execute(request.toCommand())
    }

}