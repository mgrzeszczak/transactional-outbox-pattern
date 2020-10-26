package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.command

import com.mgrzeszczak.transactionaloutboxpattern.domain.CommandHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommandConfig {

    @Bean
    fun commandExecutor(handlers: List<CommandHandler<*>>) = CommandExecutor(handlers)

}