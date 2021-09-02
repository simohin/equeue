package simohin.eueue.core.service.grpc

import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.*
import simohin.eueue.core.configuration.grpc.GrpcServerConfiguration
import kotlin.random.Random

@Service
class QueueItemRegistrationService : ReactorQueueItemRegistrationServiceGrpc.QueueItemRegistrationServiceImplBase(),
    GrpcServerConfiguration.GrpcService {

    private lateinit var eventSink: FluxSink<QueueItemEvent>
    private val eventFlux: Flux<QueueItemEvent> = Flux.create { sink -> eventSink = sink }

    @Bean
    @Qualifier("queueItemEventPublisher")
    fun getPublisher(): Publisher<QueueItemEvent> = eventFlux

    override fun register(request: Mono<RegisterQueueItemRequest>): Mono<RegisterQueueItemResponse> =
        request.map(this::requestToQueueItemEvent).doOnNext(eventSink::next).map {
            queueItemEventToResponse(true, it)
        }


    private fun requestToQueueItemEvent(request: RegisterQueueItemRequest) = QueueItemEvent.newBuilder().apply {
        id = UUID.newBuilder().setValue(java.util.UUID.randomUUID().toString()).build()
        this.item = QueueItem.newBuilder()
            .setId(UUID.newBuilder().setValue(java.util.UUID.randomUUID().toString()).build())
            .setValue(Random.nextLong(1000).toString())
            .build()
        type = QueueItemEvent.Type.CREATED
    }.build()

    private fun queueItemEventToResponse(isSuccess: Boolean, event: QueueItemEvent?) =
        RegisterQueueItemResponse.newBuilder().setIsSuccess(isSuccess).setItem(event?.item).build()
}
