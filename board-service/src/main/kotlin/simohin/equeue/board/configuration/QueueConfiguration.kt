package simohin.equeue.board.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.UnicastProcessor
import simohin.equeue.board.model.QueueItem
import java.util.*

@Configuration
class QueueConfiguration {

    @Bean
    fun queueItemsProcessor() = UnicastProcessor.create<QueueItem>()

    @Bean
    fun queueItems(queueItemsProcessor: UnicastProcessor<QueueItem>) = queueItemsProcessor.replay(30).autoConnect()

    companion object : Random()
}
