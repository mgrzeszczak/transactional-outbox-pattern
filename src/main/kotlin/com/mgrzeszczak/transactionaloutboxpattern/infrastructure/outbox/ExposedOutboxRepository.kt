package com.mgrzeszczak.transactionaloutboxpattern.infrastructure.outbox

import com.mgrzeszczak.transactionaloutboxpattern.config.Time
import com.mgrzeszczak.transactionaloutboxpattern.infrastructure.BaseRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.update
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import java.util.*

@Transactional
class ExposedOutboxRepository(time: Time) : BaseRepository(time), OutboxRepository {

    override fun insert(entry: OutboxEntry) {
        OutboxTable.insert {
            it[id] = entry.id
            it[creationDate] = serializeZonedDateTime(entry.creationDate)
            it[nextExecDate] = serializeZonedDateTime(entry.nextExecDate)
            it[completionDate] = entry.completionDate?.let(::serializeZonedDateTime)
            it[status] = entry.status
            it[type] = entry.type
            it[data] = entry.data
            it[retries] = entry.retries
        }
    }

    override fun update(entry: OutboxEntry) {
        OutboxTable.update({ OutboxTable.id eq entry.id }) {
            it[retries] = entry.retries
            it[status] = entry.status
            it[nextExecDate] = serializeZonedDateTime(entry.nextExecDate)
            it[completionDate] = entry.completionDate?.let(::serializeZonedDateTime)
        }
    }

    override fun findNext(now: ZonedDateTime, batchSize: Int, maxRetries: Int): List<OutboxEntry> {
        val transaction = TransactionManager.current()
        transaction.exec("lock table ${OutboxTable.tableName} in exclusive mode")
        val executionId = UUID.randomUUID().toString()
        val timestamp = serializeZonedDateTime(now)
        transaction.exec("""
            update ${OutboxTable.tableName}
            set ${OutboxTable.lockId.name} = '$executionId', ${OutboxTable.status.name} = '${OutboxEntryStatus.IN_PROGRESS}'
            where ${OutboxTable.id.name} in (
                select ${OutboxTable.id.name} from ${OutboxTable.tableName}
                where ${OutboxTable.nextExecDate.name} <= $timestamp
                and ${OutboxTable.status.name} = '${OutboxEntryStatus.OPEN}'
                and ${OutboxTable.retries.name} < $maxRetries
                limit $batchSize
            )
        """.trimIndent())
        return OutboxTable.select { OutboxTable.lockId eq executionId }
                .map { row ->
                    OutboxEntry(
                            id = row[OutboxTable.id],
                            creationDate = deserializeZonedDateTime(row[OutboxTable.creationDate])!!,
                            nextExecDate = deserializeZonedDateTime(row[OutboxTable.nextExecDate])!!,
                            completionDate = row[OutboxTable.completionDate]?.let { deserializeZonedDateTime(it) },
                            status = row[OutboxTable.status],
                            type = row[OutboxTable.type],
                            data = row[OutboxTable.data],
                            retries = row[OutboxTable.retries]
                    )
                }
    }

    override fun rescue(olderThan: ZonedDateTime) {
        OutboxTable.update({
            (OutboxTable.status eq OutboxEntryStatus.IN_PROGRESS).and(
                    OutboxTable.nextExecDate lessEq serializeZonedDateTime(olderThan)
            )
        }) {
            it[OutboxTable.lockId] = null
            it[OutboxTable.status] = OutboxEntryStatus.OPEN
        }
    }
}