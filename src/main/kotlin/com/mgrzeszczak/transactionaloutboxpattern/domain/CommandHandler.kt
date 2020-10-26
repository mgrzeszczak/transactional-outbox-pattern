package com.mgrzeszczak.transactionaloutboxpattern.domain

import kotlin.reflect.KClass

interface Command

interface CommandHandler<C : Command> {

    fun handle(command: C)

    fun supports(): KClass<C>

}