package simohin.eueue.core.service

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import simohin.equeue.core.lib.grpc.QueueItemEvent
import simohin.equeue.core.lib.util.Logger
import simohin.eueue.core.entity.QueueItem
import simohin.eueue.core.repository.QueueItemRepository
import javax.annotation.PostConstruct

@Service
class QueueItemRedisSaveService(
    private val repository: QueueItemRepository,
    @Qualifier("queueItemEventPublisher")
    private val publisher: Publisher<QueueItemEvent>
) : Subscriber<QueueItemEvent> {

    private val requestNumber = 1L
    private lateinit var subscription: Subscription

    override fun onSubscribe(subscription: Subscription) {
        this.subscription = subscription
        this.subscription.request(requestNumber)
    }

    override fun onNext(event: QueueItemEvent) {
        if (QueueItemEvent.Type.CREATED == event.type) repository.save(QueueItem(event.item))
        subscription.request(requestNumber)
    }

    override fun onError(error: Throwable) = throw error

    override fun onComplete() = log.debug("DONE")

    @PostConstruct
    private fun run() = publisher.subscribe(this)

    companion object : Logger()

}
