package simohin.eueue.core.grpc.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.*
import simohin.eueue.core.grpc.configuration.ReactiveGrpcServerConfiguration
import simohin.eueue.core.service.QueueItemEventService
import simohin.eueue.core.service.QueueItemService
import kotlin.random.Random

@Service
class QueueItemRegistrationService(
    private val queueItemService: QueueItemService,
    private val queueItemEventService: QueueItemEventService
) : ReactorQueueItemRegistrationServiceGrpc.QueueItemRegistrationServiceImplBase(),
    ReactiveGrpcServerConfiguration.GrpcService {

    override fun register(request: Mono<RegisterQueueItemRequest>): Mono<RegisterQueueItemResponse> =
        request.map {
            QueueItem.newBuilder()
                .setId(UUID.newBuilder().setValue(java.util.UUID.randomUUID().toString()).build())
                .setValue(Random.nextLong(1000).toString())
                .build()
        }.publish(queueItemService::onNew).map {
            QueueItemEvent.newBuilder().apply {
                id = UUID.newBuilder().setValue(java.util.UUID.randomUUID().toString()).build()
                this.item = it
                type = QueueItemEvent.Type.CREATED
            }.build()
        }.publish(queueItemEventService::publish)
            .map { RegisterQueueItemResponse.newBuilder().setIsSuccess(true).setItem(it.item).build() }
}
