package com.mgrzeszczak.transactionaloutboxpattern.domain

interface EventPublisher {
    fun publish(event: Event)
}

interface Event {
    fun key(): String
    fun version(): Int
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Topic(val value: String)