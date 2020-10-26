package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.order.repository.OrderTable
import org.jetbrains.exposed.sql.Table

object OutboxTable : Table("outbox") {

    val id = varchar("id", 36)
    val creationDate = long("creation_date")
    val nextExecDate = long("next_exec_date")
    val completionDate = long("completion_date").nullable()
    val status = enumerationByName("status", 128, OutboxEntryStatus::class)
    val type = enumerationByName("type", 128, OutboxEntryType::class)
    val lockId = varchar("lock_id", 36).nullable()
    val data = text("data")
    val retries = integer("retries")

    override val primaryKey = PrimaryKey(OrderTable.id)

}