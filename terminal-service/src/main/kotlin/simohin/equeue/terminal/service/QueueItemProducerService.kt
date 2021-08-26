package simohin.equeue.terminal.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import simohin.equeue.terminal.model.QueueItem
import java.util.*

@Service
class QueueItemProducerService(
    private val queueItemProducerTemplate: ReactiveKafkaProducerTemplate<String, QueueItem>,
    @Value(value = "\${queue-item.topic-name:queue-item.topic}")
    private val topic: String
) {

    fun send(item: QueueItem) =
        queueItemProducerTemplate.send(topic, UUID.randomUUID().toString(), item)

}
