package simohin.eueue.core.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.QueueItem
import simohin.equeue.core.lib.util.Logger

@Service
class QueueItemService {

    fun onNew(itemMono: Mono<QueueItem>) = itemMono.also {
        it.subscribe(this::doOnNew)
    }

    fun doOnNew(item: QueueItem) {
        log.debug("New item:\n$item")
    }

    companion object : Logger()
}
