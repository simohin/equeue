package simohin.equeue.terminal.configuration.kafka

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.support.serializer.StringOrBytesSerializer
import reactor.core.publisher.Sinks
import reactor.kafka.sender.SenderOptions
import simohin.equeue.terminal.model.QueueItem

@Configuration
class QueueItemProducerConfiguration {

    @Bean
    fun queueItemProducerTemplate(properties: KafkaProperties) = ReactiveKafkaProducerTemplate<String, QueueItem>(
        SenderOptions.create(properties.buildProducerProperties().also {
            it["key.serializer"] = StringOrBytesSerializer::class.java
            it["value.serializer"] = JsonSerializer::class.java
        })
    )
}
