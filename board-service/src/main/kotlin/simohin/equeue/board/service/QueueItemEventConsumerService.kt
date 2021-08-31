package simohin.equeue.board.service

import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Service
import reactor.core.Disposable
import reactor.core.publisher.Sinks
import simohin.equeue.core.lib.model.kafka.QueueItem
import simohin.equeue.core.lib.model.kafka.event.QueueItemEvent
import java.util.*
import java.util.logging.Logger
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class QueueItemEventConsumerService(
    private val queueItemEventConsumerTemplate: ReactiveKafkaConsumerTemplate<UUID, QueueItemEvent>,
    private val queueItemsSink: Sinks.Many<QueueItem>
) {

    private var sub: Disposable? = null

    @PostConstruct
    fun run() {

        sub = queueItemEventConsumerTemplate.receiveAutoAck()
            .map { it.value() }.doOnNext {
                log.info("Got new $it")
            }.subscribe {
                queueItemsSink.emitNext(it.item) { type, result -> throw RuntimeException("Failed to emit with signal: $type and result: $result") }
            }

    }

    @PreDestroy
    fun stop() = sub!!.dispose()

    companion object {
        val log: Logger = Logger.getLogger(QueueItemEventConsumerService::class.java.simpleName)
    }
}
