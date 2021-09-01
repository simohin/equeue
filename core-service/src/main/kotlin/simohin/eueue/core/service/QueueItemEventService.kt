package simohin.eueue.core.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.QueueItemEvent
import simohin.equeue.core.lib.util.Logger

@Service
class QueueItemEventService {

    fun publish(eventMono: Mono<QueueItemEvent>) = eventMono.also {
        it.subscribe(this::doPublish)
    }

    fun doPublish(event: QueueItemEvent) {
        log.debug("New event:\n$event")
    }

    companion object : Logger()
}
