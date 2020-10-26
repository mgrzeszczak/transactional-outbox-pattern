package com.mgrzeszczak.transactionaloutboxpattern.domain.order

import com.mgrzeszczak.transactionaloutboxpattern.domain.Command
import com.mgrzeszczak.transactionaloutboxpattern.domain.valueobject.Money

data class CreateOrderCommand(val amount: Money) : Command