package simohin.equeue.terminal.integration.core.grpc.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.ReactorQueueItemRegistrationServiceGrpc
import simohin.equeue.core.lib.grpc.RegisterQueueItemRequest
import simohin.equeue.core.lib.grpc.RegisterQueueItemResponse

@Service
class QueueItemRegistrationService(private val queueItemRegistrationServiceStub: ReactorQueueItemRegistrationServiceGrpc.ReactorQueueItemRegistrationServiceStub) {

    fun register(requestMono: Mono<RegisterQueueItemRequest>): Mono<RegisterQueueItemResponse> =
        queueItemRegistrationServiceStub.register(requestMono)
}
