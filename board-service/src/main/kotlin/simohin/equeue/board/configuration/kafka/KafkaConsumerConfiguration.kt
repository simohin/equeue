package simohin.equeue.board.configuration.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.ReceiverOptions
import simohin.equeue.core.lib.model.kafka.event.QueueItemEvent
import java.util.*

@Configuration
class KafkaConsumerConfiguration(
    private val properties: KafkaProperties,
    @Value("\${spring.application.name}")
    private val groupId: String,
    @Value(value = "\${event.queue-item.topic-name:event.queue-item.topic}")
    private val topic: String
) {

    @Bean
    fun queueItemEventConsumerTemplate(): ReactiveKafkaConsumerTemplate<UUID, QueueItemEvent> =
        ReactiveKafkaConsumerTemplate(
            kafkaReceiverOptions<UUID, QueueItemEvent>().subscription(
                Collections.singletonList(
                    topic
                )
            )
        )

    private fun <K, V> kafkaReceiverOptions(): ReceiverOptions<K, V> =
        ReceiverOptions.create(properties.buildProducerProperties().also {
            it["key.deserializer"] = JsonDeserializer::class.java
            it["value.deserializer"] = JsonDeserializer::class.java
            it["auto.offset.reset"] = "earliest"
            it["group.id"] = groupId
        })
}
