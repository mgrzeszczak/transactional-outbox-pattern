package com.mgrzeszczak.transactionaloutboxpattern

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TransactionalOutboxPatternApplication

fun main(args: Array<String>) {
    runApplication<TransactionalOutboxPatternApplication>(*args)
}
