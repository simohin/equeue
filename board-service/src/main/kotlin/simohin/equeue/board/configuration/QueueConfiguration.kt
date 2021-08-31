package simohin.equeue.board.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Sinks
import simohin.equeue.core.lib.model.kafka.QueueItem
import java.util.*

@Configuration
class QueueConfiguration {

    @Bean
    fun queueItemsSink(): Sinks.Many<QueueItem> = Sinks.many().multicast().onBackpressureBuffer()

    @Bean
    fun queueItemsFlux(queueItemsSink: Sinks.Many<QueueItem>) = queueItemsSink.asFlux()

    companion object : Random()
}
