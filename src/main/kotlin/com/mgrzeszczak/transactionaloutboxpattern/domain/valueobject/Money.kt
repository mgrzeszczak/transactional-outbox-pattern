package com.mgrzeszczak.transactionaloutboxpattern.domain.valueobject

import java.math.BigDecimal
import java.math.RoundingMode

data class Money private constructor(private val amount: BigDecimal) {

    private val scale: Int = 2
    val value = amount.setScale(scale, RoundingMode.DOWN)

    companion object {
        fun of(value: String) = Money(BigDecimal(value))

        fun of(value: Number) = Money(BigDecimal(value.toDouble()))
    }

    override fun toString() = value.toString()


}