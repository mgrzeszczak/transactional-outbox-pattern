package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.command

import com.mgrzeszczak.transactionaloutboxpattern.domain.Command
import com.mgrzeszczak.transactionaloutboxpattern.domain.CommandHandler

class CommandExecutor(handlers: List<CommandHandler<*>>) {

    private val commandHandlerMap = handlers.associateBy { it.supports() }

    fun <C : Command> execute(command: C) {
        (commandHandlerMap[command::class] as CommandHandler<C>?)?.handle(command)
    }

}