package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order.repository

import org.jetbrains.exposed.sql.Table

object OrderTable : Table("order") {

    val id = varchar("id", length = 36)
    val amount = decimal("amount", 19, 2)
    val creationDate = long("creation_date")

    override val primaryKey = PrimaryKey(id)

}