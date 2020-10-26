package com.mgrzeszczak.transactionaloutboxpattern.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mgrzeszczak.transactionaloutboxpattern.domain.valueobject.Money
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.ZonedDateTime

@Configuration
class JsonConfig {

    @Bean
    fun objectMapper(time: Time): ObjectMapper = jacksonObjectMapper()
            .registerModule(moneyModule())
            .registerModule(timeModule(time))

    private fun moneyModule() = SimpleModule()
            .addSerializer(Money::class.java, MoneySerializer())
            .addDeserializer(Money::class.java, MoneyStringDeserializer())

    private fun timeModule(time: Time) = SimpleModule()
            .addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer())
            .addDeserializer(ZonedDateTime::class.java, ZonedDateTimeStringDeserializer(time))

}

open class ValueObjectStringDeserializer<T>(val mapping: (String) -> T) : JsonDeserializer<T>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        return mapping(p.valueAsString)
    }

}

open class ValueObjectLongDeserializer<T>(val mapping: (Long) -> T) : JsonDeserializer<T>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        return mapping(p.longValue)
    }

}

open class ValueObjectSerializer<T, R>(val mapping: (T) -> R) : JsonSerializer<T>() {

    override fun serialize(value: T, gen: JsonGenerator, serializers: SerializerProvider) {
        when (val result = mapping(value)) {
            is Long -> gen.writeNumber(result)
            else -> gen.writeString(result.toString())
        }
    }

}

class MoneySerializer : ValueObjectSerializer<Money, String>(Money::toString)
class MoneyStringDeserializer : ValueObjectStringDeserializer<Money>(Money.Companion::of)

class ZonedDateTimeSerializer : ValueObjectSerializer<ZonedDateTime, Long>({ it.toEpochSecond() })
class ZonedDateTimeStringDeserializer(private val time: Time) : ValueObjectLongDeserializer<ZonedDateTime>({ time.toZonedDateTime(it) })